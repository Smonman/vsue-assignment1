package dslab.monitoring;

import dslab.monitoring.storage.Storage;
import dslab.util.CloseableResource;
import dslab.util.parser.PacketDataParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * An extension of {@code Thread}.
 *
 * <p>This is a listener thread that will receive incoming datagram packets,
 * with a size less than 1024 Bytes.
 *
 * @see Thread
 */
public class ListenerThread extends Thread implements CloseableResource {

    private static final Log LOG =
            LogFactory.getLog(MethodHandles.lookup().lookupClass());
    private final DatagramSocket datagramSocket;
    private final Storage addressStorage;
    private final Storage serverStorage;

    public ListenerThread(final DatagramSocket datagramSocket,
                          final Storage addressStorage,
                          final Storage serverStorage) {
        this.datagramSocket = datagramSocket;
        this.addressStorage = addressStorage;
        this.serverStorage = serverStorage;
    }

    @Override
    public void run() {
        byte[] buffer;
        DatagramPacket packet;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                buffer = new byte[1024];
                packet = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(packet);
                addressStorage.increment(
                        PacketDataParser.getAddress(getPacketData(packet)));
                serverStorage.increment(
                        PacketDataParser.getHostPortPair(getPacketData(packet)));
            }
        } catch (SocketException e) {
            LOG.warn("An error occurred while waiting for or handling packet",
                    e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            closeCloseable(datagramSocket);
        }
    }

    private String getPacketData(DatagramPacket packet) {
        return new String(
                Arrays.copyOfRange(packet.getData(), 0, packet.getLength()),
                StandardCharsets.UTF_8);
    }
}
