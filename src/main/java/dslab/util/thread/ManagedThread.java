package dslab.util.thread;

import dslab.protocol.general.ExtendableErrorResponse;
import dslab.protocol.general.ExtendableOkResponse;
import dslab.util.CloseableResource;
import dslab.util.socketmanager.SocketManager;
import dslab.util.wrapper.PrintWrapper;
import dslab.util.wrapper.ReaderWrapper;

import java.net.Socket;

/**
 * This is an implementation of Thread that is managed via a
 * SocketManager.
 *
 * @see SocketManager
 */
public abstract class ManagedThread extends Thread implements PrintWrapper,
        ReaderWrapper, CloseableResource,
        ExtendableOkResponse, ExtendableErrorResponse {

    private final SocketManager socketManager;
    private final int index;

    public ManagedThread(final Socket socket,
                         final SocketManager socketManager) {
        this.socketManager = socketManager;
        this.index = socketManager.put(socket);
    }

    /**
     * Removes this from the socket manager after the run hook.
     *
     * @see this#runHook()
     */
    @Override
    public final void run() {
        runHook();
        socketManager.remove(index);
    }

    /**
     * This hook is called on {@code Thread#run()}.
     *
     * @see Thread#run()
     */
    public abstract void runHook();
}
