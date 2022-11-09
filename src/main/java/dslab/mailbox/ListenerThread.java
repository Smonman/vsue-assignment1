package dslab.mailbox;

import dslab.ReaderThreadFactory;
import dslab.mailbox.lookup.UserLookup;
import dslab.mailbox.storage.Storage;
import dslab.util.CloseableResource;
import dslab.util.Config;
import dslab.util.wrapper.PrintWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public final class ListenerThread extends Thread
    implements PrintWrapper, CloseableResource {

    private static final Log LOG =
        LogFactory.getLog(MethodHandles.lookup().lookupClass());
    private final ServerSocket serverSocket;
    private final String readerThreadType;
    private final Config config;
    private final UserLookup userLookup;
    private final Storage storage;
    private Socket socket;

    public ListenerThread(final ServerSocket serverSocket,
                          final String readerThreadType,
                          final Config config,
                          final UserLookup userLookup,
                          final Storage storage) {
        this.serverSocket = serverSocket;
        this.readerThreadType = readerThreadType;
        this.config = config;
        this.userLookup = userLookup;
        this.storage = storage;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                socket = serverSocket.accept();
                ReaderThreadFactory.createReaderThread(readerThreadType, socket,
                    config, userLookup, storage).start();
            } catch (SocketException e) {
                LOG.error("SocketException while handling socket", e);
                break;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (RuntimeException e) {
                LOG.error("An error occurred", e);
                break;
            }
        }
        shutdown();
    }

    private void shutdown() {
        closeCloseable(socket);
    }
}
