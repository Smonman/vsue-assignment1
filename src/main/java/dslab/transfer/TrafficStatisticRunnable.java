package dslab.transfer;

import dslab.util.CloseableResource;
import dslab.util.Config;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;

public final class TrafficStatisticRunnable
        implements Runnable, CloseableResource {

    private final String host;
    private final int port;
    private final String address;
    private final String monitoringHost;
    private final int monitoringPort;

    public TrafficStatisticRunnable(final String host,
                                    final int port,
                                    final String address,
                                    final Config senderConfig) {
        this.host = host;
        this.port = port;
        this.address = address;
        this.monitoringHost = senderConfig.getString("monitoring.host");
        this.monitoringPort = senderConfig.getInt("monitoring.port");
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        byte[] buffer;
        DatagramPacket packet;
        try {
            socket = new DatagramSocket();
            buffer = createPacketData().getBytes();
            packet = new DatagramPacket(buffer,
                    buffer.length,
                    InetAddress.getByName(monitoringHost),
                    monitoringPort);
            socket.send(packet);
        } catch (SocketException e) {
            LOG.warn("SocketException while handling socket", e);
        } catch (UnknownHostException e) {
            LOG.error("Cannot connect to host", e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            closeCloseable(socket);
        }
    }

    private String createPacketData() {
        return String.format("%s:%d %s", host, port, address);
    }
}
