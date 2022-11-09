package dslab.transfer;

import dslab.protocol.dmtp.message.DMTPMessage;
import dslab.transfer.lookup.DomainLookup;
import dslab.transfer.socket.SocketManager;
import dslab.util.CloseableResource;
import dslab.util.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An extension of {@code Thread}.
 *
 * <p>This is a listener thread that will accept incoming connections on the
 * server socket.
 *
 * @see Thread
 */
public final class ListenerThread extends Thread implements CloseableResource {

    private static final Log LOG =
        LogFactory.getLog(MethodHandles.lookup().lookupClass());
    private static final int POOL_SIZE = 8;
    private final ServerSocket serverSocket;
    private final BlockingQueue<DMTPMessage> messageQueue;
    private final DomainLookup domainLookup;
    private final ExecutorService producerPool;
    private final ExecutorService consumerPool;
    private final Config config;
    private final SocketManager socketManager;

    public ListenerThread(final ServerSocket serverSocket,
                          final Config config) {
        this.serverSocket = serverSocket;
        this.config = config;
        this.messageQueue = new ArrayBlockingQueue<>(POOL_SIZE * 2);
        this.domainLookup = new DomainLookup();
        this.producerPool = Executors.newFixedThreadPool(POOL_SIZE);
        this.consumerPool = Executors.newFixedThreadPool(POOL_SIZE);
        this.socketManager = new SocketManager();
    }

    @Override
    public void run() {
        startConsumer();
        Socket socket = null;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                socket = serverSocket.accept();
                producerPool.execute(new DMTPReaderThread(
                    socket, messageQueue, socketManager));
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

    private void startConsumer() {
        for (int i = 0; i < POOL_SIZE; i++) {
            consumerPool.execute(new DMTPForwarderThread(
                config, domainLookup, messageQueue));
        }
    }

    private void shutdown() {
        closeCloseable(socketManager);
        shutdownAndAwaitTermination(consumerPool);
        shutdownAndAwaitTermination(producerPool);
    }
}
