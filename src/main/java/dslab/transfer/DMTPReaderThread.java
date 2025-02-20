package dslab.transfer;

import dslab.protocol.dmap.instruction.Hook;
import dslab.protocol.dmtp.instruction.map.DMTPInstructionMap;
import dslab.protocol.dmtp.message.DMTPMessage;
import dslab.protocol.dmtp.parser.DMTPParser;
import dslab.protocol.general.exception.InstructionNotFoundException;
import dslab.protocol.general.exception.InvalidInstructionException;
import dslab.util.socketmanager.SocketManager;
import dslab.util.thread.ManagedThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

public final class DMTPReaderThread extends ManagedThread {
    private static final Log LOG =
            LogFactory.getLog(MethodHandles.lookup().lookupClass());
    private final Socket socket;
    private final BlockingQueue<DirectedDMTPMesssage> messagesQueue;
    private final Hook<Boolean, String> acceptHook;
    private final Hook<Boolean, String> isKnownHook;
    private DMTPParser dmtpParser;

    public DMTPReaderThread(final Socket socket,
                            final BlockingQueue<DirectedDMTPMesssage> messageQueue,
                            final SocketManager socketManager) {
        super(socket, socketManager);
        this.socket = socket;
        this.messagesQueue = messageQueue;
        this.acceptHook = s -> true;
        this.isKnownHook = s -> true;
        this.dmtpParser =
                new DMTPParser(new DMTPInstructionMap(acceptHook, isKnownHook));
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
    public void runHook() {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            printlnToPrintWriter(writer, okResponse());
            String instruction;
            while ((instruction = readLineFromBufferedReader(reader)) != null) {
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
                    addMessagesPerDomainToQueue(dmtpParser.getMessage());
                    dmtpParser = new DMTPParser(
                            new DMTPInstructionMap(acceptHook, isKnownHook));
                }
            }
        } catch (SocketException e) {
            LOG.warn("SocketException while handling socket", e);
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
        }
    }

    private void addMessagesPerDomainToQueue(final DMTPMessage message)
            throws InterruptedException {
        for (String domain : message.getToDomains()) {
            messagesQueue.put(new DirectedDMTPMesssage(message, domain));
        }
    }
}
