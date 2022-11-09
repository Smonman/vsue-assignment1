package dslab;

import dslab.mailbox.DMAPReaderThread;
import dslab.mailbox.DMTPReaderThread;
import dslab.mailbox.lookup.UserLookup;
import dslab.mailbox.storage.Storage;
import dslab.transfer.socket.SocketManager;
import dslab.util.Config;

import java.net.Socket;

/**
 * This is a static utility class.
 *
 * <p>It provides a way to create new reader threads based on the given
 * reader thread type.
 *
 * @see ReaderThreadFactory.readerThreadType
 * @see dslab.mailbox.DMAPReaderThread
 * @see dslab.mailbox.DMTPReaderThread
 */
public final class ReaderThreadFactory {
    private ReaderThreadFactory() {
    }

    /**
     * Creates a new reader Thread based on the given type.
     *
     * @param type          the type of the new thread
     * @param socket        the accepted socket
     * @param config        the config of the server, only used for the dmtp reader
     *                      thread
     * @param userLookup    a user lookup object
     * @param storage       a storage object
     * @param socketManager a socket manager object
     * @return a new reader thread of the given type
     */
    public static Thread createReaderThread(final readerThreadType type,
                                            final Socket socket,
                                            final Config config,
                                            final UserLookup userLookup,
                                            final Storage storage,
                                            final SocketManager socketManager) {
        switch (type) {
            case DMTP:
                return new DMTPReaderThread(
                    socket,
                    config,
                    userLookup,
                    storage,
                    socketManager
                );
            case DMAP:
            default:
                return new DMAPReaderThread(
                    socket,
                    userLookup,
                    storage,
                    socketManager
                );
        }
    }

    public enum readerThreadType {DMTP, DMAP}
}
