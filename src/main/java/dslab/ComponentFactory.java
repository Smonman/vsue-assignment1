package dslab;

import dslab.mailbox.IMailboxServer;
import dslab.mailbox.MailboxServer;
import dslab.monitoring.IMonitoringServer;
import dslab.monitoring.MonitoringServer;
import dslab.transfer.ITransferServer;
import dslab.transfer.TransferServer;
import dslab.util.Config;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * The component factory provides methods to create the core components of the application. You can edit the method body
 * if the component instantiation requires additional logic.
 *
 * Do not change the existing method signatures!
 */
public final class ComponentFactory {

    private ComponentFactory() {
        // static utility class
    }

    /**
     * Creates a new {@link IMonitoringServer} instance.
     *
     * @param componentId the component id
     * @param in          the input stream used for accepting cli commands
     * @param out         the output stream to print to
     * @return a new MonitoringServer instance
     */
    public static IMonitoringServer createMonitoringServer(String componentId,
                                                           InputStream in,
                                                           PrintStream out)
        throws Exception {
        Config config = new Config(componentId);
        return new MonitoringServer(componentId, config, in, out);
    }

    /**
     * Creates a new {@link IMailboxServer} instance.
     *
     * @param componentId the component id
     * @param in          the input stream used for accepting cli commands
     * @param out         the output stream to print to
     * @return a new MailboxServer instance
     */
    public static IMailboxServer createMailboxServer(String componentId,
                                                     InputStream in,
                                                     PrintStream out)
        throws Exception {
        Config config = new Config(componentId);
        return new MailboxServer(componentId, config, in, out);
    }

    /**
     * Creates a new {@link ITransferServer} instance.
     *
     * @param componentId the component id
     * @param in          the input stream used for accepting cli commands
     * @param out         the output stream to print to
     * @return a new TransferServer instance
     */
    public static ITransferServer createTransferServer(String componentId,
                                                       InputStream in,
                                                       PrintStream out)
        throws Exception {
        Config config = new Config(componentId);
        return new TransferServer(componentId, config, in, out);
    }

}
