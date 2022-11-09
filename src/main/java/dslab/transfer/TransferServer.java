package dslab.transfer;

import at.ac.tuwien.dsg.orvell.Shell;
import at.ac.tuwien.dsg.orvell.StopShellException;
import at.ac.tuwien.dsg.orvell.annotation.Command;
import dslab.ComponentFactory;
import dslab.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;

public final class TransferServer implements ITransferServer {

    private final Config config;
    private final Shell shell;
    private final int port;
    private ServerSocket serverSocket;

    /**
     * Creates a new server instance.
     *
     * @param componentId the id of the component that corresponds to the Config
     *                    resource
     * @param config      the component config
     * @param in          the input stream to read console input from
     * @param out         the output stream to write console output to
     */
    public TransferServer(final String componentId,
                          final Config config,
                          final InputStream in,
                          final PrintStream out) {
        this.config = config;
        this.shell = new Shell(in, out);
        this.shell.register(this);
        this.shell.setPrompt(String.format("%s> ", componentId));
        this.port = config.getInt("tcp.port");
        this.serverSocket = null;
    }

    public static void main(String[] args) throws Exception {
        ITransferServer server =
            ComponentFactory.createTransferServer(args[0], System.in,
                System.out);
        server.run();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            new ListenerThread(serverSocket, config).start();
        } catch (IOException e) {
            throw new UncheckedIOException(
                "An error occurred while creating the server socket.", e);
        }
        shell.out()
            .printf("The server is up on port %d!\n", port);
        shell.run();
    }

    @Override
    @Command
    public void shutdown() {
        closeCloseable(serverSocket);
        throw new StopShellException();
    }
}
