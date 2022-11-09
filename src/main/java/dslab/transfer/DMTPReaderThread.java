package dslab.transfer;

import dslab.protocol.dmap.instruction.Hook;
import dslab.protocol.dmtp.instruction.map.DMTPInstructionMap;
import dslab.protocol.dmtp.message.DMTPMessage;
import dslab.protocol.dmtp.parser.DMTPParser;
import dslab.protocol.general.ExtendableErrorResponse;
import dslab.protocol.general.ExtendableOkResponse;
import dslab.protocol.general.exception.InstructionNotFoundException;
import dslab.protocol.general.exception.InvalidInstructionException;
import dslab.transfer.socket.SocketManager;
import dslab.util.CloseableResource;
import dslab.util.wrapper.PrintWrapper;
import dslab.util.wrapper.ReaderWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class DMTPReaderThread extends Thread
    implements PrintWrapper, ReaderWrapper, CloseableResource,
    ExtendableOkResponse, ExtendableErrorResponse {
    private static final Log LOG =
        LogFactory.getLog(MethodHandles.lookup().lookupClass());
    private final Socket socket;
    private final BlockingQueue<DMTPMessage> messagesQueue;
    private final Hook<Boolean, String> acceptHook;
    private final Hook<Boolean, String> isKnownHook;
    private final SocketManager socketManager;
    private final int index;
    private DMTPParser dmtpParser;

    public DMTPReaderThread(final Socket socket,
                            final BlockingQueue<DMTPMessage> messageQueue,
                            final SocketManager socketManager) {
        this.socket = socket;
        this.messagesQueue = messageQueue;
        this.acceptHook = s -> true;
        this.isKnownHook = s -> true;
        this.dmtpParser =
            new DMTPParser(new DMTPInstructionMap(acceptHook, isKnownHook));
        this.socketManager = socketManager;
        this.index = socketManager.put(socket);
    }

    @Override
    public String errorResponseExtension() {
        return "protocol error";
    }

    @Override
    public String okResponseExtension() {
        return "DMTP";
    }

    @Override
    public void run() {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            printlnToPrintWriter(writer, okResponse());
            String instruction;
            while ((instruction = reader.readLine()) != null) {
                try {
                    dmtpParser.parseInstruction(instruction);
                } catch (InstructionNotFoundException e) {
                    LOG.error(e);
                    printlnToPrintWriter(writer, errorResponse());
                    break;
                }
                printlnToPrintWriter(writer, dmtpParser.getResponse());
                if (dmtpParser.isSet("quit")) {
                    LOG.debug("quit is set, exiting");
                    break;
                }
                if (dmtpParser.isSet("send") && dmtpParser.isComplete()) {
                    LOG.debug("send is set and message is complete");
                    addMessagesToQueue(dmtpParser.getMessages());
                    dmtpParser = new DMTPParser(
                        new DMTPInstructionMap(acceptHook, isKnownHook));
                }
            }
        } catch (SocketException e) {
            LOG.error("SocketException while handling socket", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InvalidInstructionException e) {
            LOG.error("Error invalid instruction", e);
        } catch (RuntimeException e) {
            LOG.error("An error occurred", e);
        } catch (InterruptedException e) {
            LOG.warn("An interrupt occurred", e);
        } finally {
            closeCloseable(reader);
            closeCloseable(writer);
            closeCloseable(socket);
            socketManager.remove(index);
        }
    }
    
    private void addMessagesToQueue(final List<DMTPMessage> messages)
        throws InterruptedException {
        for (DMTPMessage message : messages) {
            messagesQueue.put(message);
        }
    }
}
