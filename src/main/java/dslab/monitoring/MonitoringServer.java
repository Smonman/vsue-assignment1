package dslab.monitoring;

import at.ac.tuwien.dsg.orvell.Shell;
import at.ac.tuwien.dsg.orvell.StopShellException;
import at.ac.tuwien.dsg.orvell.annotation.Command;
import dslab.ComponentFactory;
import dslab.monitoring.storage.Storage;
import dslab.monitoring.storage.impl.InMemoryStorage;
import dslab.util.CloseableResource;
import dslab.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.DatagramSocket;

public final class MonitoringServer implements IMonitoringServer,
        CloseableResource {

    private final Shell shell;
    private final int port;
    private final Storage addressStorage;
    private final Storage serverStorage;
    private DatagramSocket datagramSocket;

    /**
     * Creates a new server instance.
     *
     * @param componentId the id of the component that corresponds to the Config
     *                    resource
     * @param config      the component config
     * @param in          the input stream to read console input from
     * @param out         the output stream to write console output to
     */
    public MonitoringServer(final String componentId,
                            final Config config,
                            final InputStream in,
                            final PrintStream out) {
        this.shell = new Shell(in, out);
        this.shell.register(this);
        this.shell.setPrompt(String.format("%s> ", componentId));
        this.port = config.getInt("udp.port");
        this.datagramSocket = null;
        this.addressStorage = new InMemoryStorage();
        this.serverStorage = new InMemoryStorage();
    }

    public static void main(String[] args) throws Exception {
        IMonitoringServer server =
                ComponentFactory.createMonitoringServer(args[0], System.in,
                        System.out);
        server.run();
    }

    @Override
    public void run() {
        try {
            datagramSocket = new DatagramSocket(port);
            new ListenerThread(datagramSocket, addressStorage, serverStorage)
                    .start();
        } catch (IOException e) {
            throw new RuntimeException("Error cannot listen on UDP port", e);
        }
        shell.out().printf("The server is up on port %d!\n", port);
        shell.run();
    }

    @Override
    @Command
    public void shutdown() {
        closeCloseable(datagramSocket);
        throw new StopShellException();
    }

    @Override
    @Command
    public void servers() {
        serverStorage
                .dump()
                .forEach(e -> shell.out().println(e));
    }

    @Override
    @Command
    public void addresses() {
        addressStorage
                .dump()
                .forEach(e -> shell.out().println(e));
    }
}
