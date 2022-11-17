package dslab.util.socketmanager;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is a thread safe storage for closeables.
 */
public class SocketManager implements Closeable {

    private final Map<Integer, Closeable> silo;
    private final AtomicInteger index;

    public SocketManager() {
        this.silo = new ConcurrentHashMap<>();
        this.index = new AtomicInteger(0);
    }

    /**
     * Adds a new closeable to this.
     *
     * <p>After this method the given closeable can be removed via the
     * returned key.
     *
     * @param value the given closeable to be stored
     * @return the key of the given value
     */
    public int put(Closeable value) {
        int newIndex = index.getAndIncrement();
        silo.put(newIndex, value);
        return newIndex;
    }

    /**
     * Removes the value associated with the given key.
     *
     * <p>Before removing the closeable, it will be closed.
     *
     * @param key the key of the value to be removed.
     */
    public void remove(int key) {
        if (silo.containsKey(key)) {
            try {
                Closeable c = silo.get(key);
                if (c != null) {
                    c.close();
                }
            } catch (IOException ignored) {
            }
            silo.remove(key);
        }
    }

    /**
     * Closes all closeables in this.
     *
     * <p>After a successful call no closeables are found in this.
     *
     * @throws IOException if an I/O exception occurs while closing
     */
    @Override
    public void close() throws IOException {
        // as loop instead of stream beause of the IOException
        for (Closeable c : silo.values()) {
            c.close();
        }
        silo.clear();
    }
}
