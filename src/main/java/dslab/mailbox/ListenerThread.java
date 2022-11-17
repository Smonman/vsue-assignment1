package dslab.mailbox;

import dslab.ReaderThreadFactory;
import dslab.mailbox.lookup.UserLookup;
import dslab.mailbox.storage.Storage;
import dslab.util.CloseableResource;
import dslab.util.Config;
import dslab.util.socketmanager.SocketManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ListenerThread extends Thread
        implements CloseableResource {

    private static final Log LOG =
            LogFactory.getLog(MethodHandles.lookup().lookupClass());
    private static final int POOL_SIZE = 16;
    private final ServerSocket serverSocket;
    private final ReaderThreadFactory.readerThreadType readerThreadType;
    private final Config config;
    private final UserLookup userLookup;
    private final Storage storage;
    private final ExecutorService pool;
    private final SocketManager socketManager;
    private Socket socket;

    public ListenerThread(final ServerSocket serverSocket,
                          final ReaderThreadFactory.readerThreadType readerThreadType,
                          final Config config,
                          final UserLookup userLookup,
                          final Storage storage) {
        this.serverSocket = serverSocket;
        this.readerThreadType = readerThreadType;
        this.config = config;
        this.userLookup = userLookup;
        this.storage = storage;
        this.pool = Executors.newFixedThreadPool(POOL_SIZE);
        this.socketManager = new SocketManager();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                socket = serverSocket.accept();
                pool.execute(
                        ReaderThreadFactory.createReaderThread(readerThreadType,
                                socket, config, userLookup, storage, socketManager));
            } catch (SocketException e) {
                LOG.warn("SocketException while handling socket", e);
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
        closeCloseable(socketManager);
        shutdownAndAwaitTermination(pool);
        closeCloseable(socket);
    }
}
