package dslab.transfer.socket;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketManager implements Closeable {

    private final Map<Integer, Socket> silo;

    public SocketManager() {
        this.silo = new ConcurrentHashMap<>();
    }

    public void put(Thread t, Socket s) {

    }

    public void remove(Integer key) {

    }

    @Override
    public void close() throws IOException {
        //silo.values().forEach(Socket::close);
    }
}
