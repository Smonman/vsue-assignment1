package dslab.mailbox;

import dslab.mailbox.lookup.UserLookup;
import dslab.mailbox.storage.Storage;
import dslab.mailbox.storage.key.impl.TwoPartKey;
import dslab.protocol.dmap.instruction.Hook;
import dslab.protocol.dmap.instruction.map.DMAPInstructionMap;
import dslab.protocol.dmap.parser.DMAPParser;
import dslab.protocol.general.exception.InstructionNotFoundException;
import dslab.protocol.general.exception.InvalidInstructionException;
import dslab.transfer.socket.SocketManager;
import dslab.util.parser.LoginParser;
import dslab.util.thread.ManagedThread;
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
import java.util.stream.Collectors;

public final class DMAPReaderThread extends ManagedThread {
    private static final Log LOG =
        LogFactory.getLog(MethodHandles.lookup().lookupClass());
    private final Socket socket;
    private final DMAPParser dmapParser;
    private final Hook<Void, String> deleteHook;
    private final Hook<String, String> showHook;
    private final Hook<Boolean, String> loginHook;
    private final Hook<String, String> listHook;
    private final Hook<Boolean, String> existsHook;
    private String loggedInUser;

    public DMAPReaderThread(final Socket socket,
                            final UserLookup userLookup,
                            final Storage storage,
                            final SocketManager socketManager) {
        super(socket, socketManager);
        this.socket = socket;
        this.loggedInUser = null;
        this.deleteHook = s -> {
            storage.remove(TwoPartKey.of(Integer.parseInt(s), loggedInUser));
            return null;
        };
        this.showHook = s ->
            storage.get(TwoPartKey.of(Integer.parseInt(s), loggedInUser))
                .getMessage().toDMAPString();
        this.loginHook = s -> {
            String username = LoginParser.getUsername(s);
            String password = LoginParser.getPassword(s);
            if (userLookup.containsKey(username) &&
                userLookup.get(username).equals(password)) {
                loggedInUser = username;
                return true;
            } else {
                loggedInUser = null;
                return false;
            }
        };
        this.listHook = s ->
            storage.getAll(loggedInUser)
                .stream()
                .map(p -> String.format("%7d: %s", p.getId(),
                    p.getMessage().toPreviewString()))
                .collect(Collectors.joining("\r\n"));
        this.existsHook = s -> storage.contains(
            TwoPartKey.of(Integer.parseInt(s), loggedInUser));
        this.dmapParser = new DMAPParser(
            new DMAPInstructionMap(deleteHook, listHook, loginHook, showHook,
                existsHook));
    }

    @Override
    public String errorResponseExtension() {
        return "protocol error";
    }

    @Override
    public String okResponseExtension() {
        return "DMAP";
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
                    dmapParser.parseInstruction(instruction);
                } catch (InstructionNotFoundException e) {
                    LOG.error(e);
                    printlnToPrintWriter(writer, errorResponse());
                    break;
                }
                printlnToPrintWriter(writer, dmapParser.getResponse());
                if (dmapParser.isSet("quit")) {
                    break;
                }
            }
        } catch (SocketException e) {
            LOG.error("SocketException while handling socket", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InvalidInstructionException e) {
            LOG.error(
                String.format("Error invalid instruction: %s", e.getMessage()),
                e);
        } catch (RuntimeException e) {
            LOG.error("An error occurred", e);
        } finally {
            closeCloseable(reader);
            closeCloseable(writer);
            closeCloseable(socket);
        }
    }
}
