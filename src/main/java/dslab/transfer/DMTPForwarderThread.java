package dslab.transfer;

import dslab.protocol.dmtp.message.DMTPMessage;
import dslab.transfer.lookup.DomainLookup;
import dslab.util.CloseableResource;
import dslab.util.Config;
import dslab.util.parser.AddressParser;
import dslab.util.wrapper.PrintWrapper;
import dslab.util.wrapper.ReaderWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public final class DMTPForwarderThread extends Thread
        implements PrintWrapper, ReaderWrapper, CloseableResource {

    private static final Log LOG =
            LogFactory.getLog(MethodHandles.lookup().lookupClass());
    private final DomainLookup domainLookup;
    private final BlockingQueue<DirectedDMTPMesssage> messageQueue;
    private final Config config;

    public DMTPForwarderThread(final Config config,
                               final DomainLookup domainLookup,
                               final BlockingQueue<DirectedDMTPMesssage> messageQueue) {
        this.config = config;
        this.domainLookup = domainLookup;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader serverReader = null;
        PrintWriter serverWriter = null;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                DirectedDMTPMesssage directedmessage = messageQueue.take();
                DMTPMessage message = directedmessage.getMessage();
                String domain = directedmessage.getDomain();
                if (!domainLookup.containsKey(domain)) {
                    LOG.warn(String.format(
                            "Warning domain lookup does not contain domain: %s\n",
                            domain));
                    sendErrorMessage(message);
                } else {
                    socket = new Socket(domainLookup.getAddress(domain),
                            domainLookup.getPort(domain));
                    serverReader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    serverWriter =
                            new PrintWriter(socket.getOutputStream());
                    String response = readLineFromBufferedReader(serverReader);
                    List<String> instructions = getReplayInstructions(message);
                    if (!response.equals("ok DMTP")) {
                        LOG.error(
                                String.format("Unexpected protocol: %s\n",
                                        response));
                    } else {
                        boolean errorOccurred = false;
                        for (int index = 0; index < instructions.size() - 1; index++) {
                            String instruction = instructions.get(index);
                            LOG.debug(
                                    String.format("Sending instruction %s",
                                            instruction));
                            printlnToPrintWriter(serverWriter, instruction);
                            response = readLineFromBufferedReader(serverReader);
                            if (response.contains("error")) {
                                LOG.debug(
                                        "Received an error response. Sending " +
                                                "error message");
                                sendErrorMessage(message);
                                errorOccurred = true;
                                break;
                            }
                        }
                        if (!errorOccurred) {
                            new TrafficStatisticRunnable(
                                    InetAddress.getLocalHost().getHostAddress(),
                                    config.getInt("tcp.port"),
                                    message.getFrom(),
                                    config
                            ).run();
                        }
                        closeCloseable(serverReader);
                        closeCloseable(serverWriter);
                        closeCloseable(socket);
                    }
                }
            }
        } catch (ConnectException e) {
            LOG.error("Server not reachable", e);
        } catch (UnknownHostException e) {
            LOG.error("Cannot connect to host", e);
        } catch (SocketException e) {
            LOG.error("SocketException while handling socket", e);
        } catch (InterruptedException e) {
            LOG.warn("An interrupt occurred", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            closeCloseable(serverReader);
            closeCloseable(serverWriter);
            closeCloseable(socket);
        }
    }

    private List<String> getReplayInstructions(final DMTPMessage message) {
        List<String> temp = new LinkedList<>();
        temp.add("begin");
        temp.addAll(message.getMessageInstructions());
        temp.add("send");
        temp.add("quit");
        return temp;
    }

    private void sendErrorMessage(final DMTPMessage message)
            throws UnknownHostException, InterruptedException {
        messageQueue.put(createErrorMessage(message));
    }

    private DirectedDMTPMesssage createErrorMessage(final DMTPMessage originalMessage)
            throws UnknownHostException {
        String newFrom = String.format("mailer@[%s]",
                InetAddress.getLocalHost().getHostAddress());
        if (originalMessage.getFrom().equals(newFrom)) {
            throw new RuntimeException("Abort error message sending");
        }
        DMTPMessage newMessage = new DMTPMessage(
                newFrom,
                List.of(originalMessage.getFrom()),
                "Error could not deliver message",
                originalMessage.toString()
        );
        return new DirectedDMTPMesssage(
                newMessage,
                AddressParser.getDomain(originalMessage.getFrom())
        );
    }
}
