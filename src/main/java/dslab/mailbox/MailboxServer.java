package dslab.mailbox;

import at.ac.tuwien.dsg.orvell.Shell;
import at.ac.tuwien.dsg.orvell.StopShellException;
import at.ac.tuwien.dsg.orvell.annotation.Command;
import dslab.ComponentFactory;
import dslab.ReaderThreadFactory;
import dslab.mailbox.lookup.UserLookup;
import dslab.mailbox.storage.Storage;
import dslab.mailbox.storage.impl.InMemoryStorage;
import dslab.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;

public final class MailboxServer implements IMailboxServer {

    private final Config config;
    private final Shell shell;
    private final int dmtpPort;
    private final int dmapPort;
    private final UserLookup userLookup;
    private final Storage storage;
    private ServerSocket dmtpServerSocket;
    private ServerSocket dmapServerSocket;

    /**
     * Creates a new server instance.
     *
     * @param componentId the id of the component that corresponds to the Config resource
     * @param config      the component config
     * @param in          the input stream to read console input from
     * @param out         the output stream to write console output to
     */
    public MailboxServer(final String componentId,
                         final Config config,
                         final InputStream in,
                         final PrintStream out) {
        this.config = config;
        this.shell = new Shell(in, out);
        this.shell.register(this);
        this.shell.setPrompt(String.format("%s> ", componentId));
        this.dmtpPort = config.getInt("dmtp.tcp.port");
        this.dmapPort = config.getInt("dmap.tcp.port");
        this.dmtpServerSocket = null;
        this.dmapServerSocket = null;
        this.userLookup = new UserLookup(config.getString("domain"));
        this.storage = new InMemoryStorage();
    }

    public static void main(String[] args) throws Exception {
        IMailboxServer server =
            ComponentFactory.createMailboxServer(args[0], System.in,
                System.out);
        server.run();
    }

    @Override
    public void run() {
        try {
            dmtpServerSocket = new ServerSocket(dmtpPort);
            dmapServerSocket = new ServerSocket(dmapPort);
            new ListenerThread(
                dmtpServerSocket,
                ReaderThreadFactory.readerThreadType.DMTP,
                config,
                userLookup,
                storage
            ).start();
            new ListenerThread(
                dmapServerSocket,
                ReaderThreadFactory.readerThreadType.DMAP,
                config,
                userLookup,
                storage
            ).start();
        } catch (IOException e) {
            throw new UncheckedIOException(
                "An error occurred while creating the server socket.", e);
        }
        shell.out().printf(
            "The server is up on DMTP port %d and DMAP port %d!\n",
            dmtpPort, dmapPort);
        shell.run();
    }

    @Override
    @Command
    public void shutdown() {
        closeCloseable(dmtpServerSocket);
        closeCloseable(dmapServerSocket);
        throw new StopShellException();
    }
}
