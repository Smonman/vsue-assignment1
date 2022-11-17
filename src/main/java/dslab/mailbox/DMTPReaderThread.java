package dslab.mailbox;

import dslab.mailbox.lookup.UserLookup;
import dslab.mailbox.storage.Storage;
import dslab.protocol.dmap.instruction.Hook;
import dslab.protocol.dmtp.instruction.map.DMTPInstructionMap;
import dslab.protocol.dmtp.message.DMTPMessage;
import dslab.protocol.dmtp.parser.DMTPParser;
import dslab.protocol.general.exception.InstructionNotFoundException;
import dslab.protocol.general.exception.InvalidInstructionException;
import dslab.util.Config;
import dslab.util.parser.AddressParser;
import dslab.util.socketmanager.SocketManager;
import dslab.util.thread.ManagedThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public final class DMTPReaderThread extends ManagedThread {

    private static final Log LOG =
            LogFactory.getLog(MethodHandles.lookup().lookupClass());
    private final Socket socket;
    private final Storage storage;
    private final String domain;
    private final Hook<Boolean, String> isKnownHook;
    private final Hook<Boolean, String> acceptHook;
    private DMTPParser dmtpParser;

    public DMTPReaderThread(final Socket socket,
                            final Config config,
                            final UserLookup userLookup,
                            final Storage storage,
                            final SocketManager socketManager) {
        super(socket, socketManager);
        this.socket = socket;
        this.storage = storage;
        this.domain = config.getString("domain");
        this.acceptHook =
                s -> AddressParser.getDomain(s).equals(domain);
        this.isKnownHook =
                s -> userLookup.containsKey(AddressParser.getUsername(s));
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
        BufferedReader reader = null;
        PrintWriter writer = null;
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
                    break;
                }
                if (dmtpParser.isSet("send") && dmtpParser.isComplete()) {
                    saveMessage(dmtpParser.getMessages());
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
        } finally {
            closeCloseable(reader);
            closeCloseable(writer);
            closeCloseable(socket);
        }
    }

    private void saveMessage(List<DMTPMessage> messages) {
        messages
                .stream()
                .filter(m -> AddressParser.getDomain(m.getPrimaryTo()).equals(domain))
                .forEach(storage::store);
    }
}
