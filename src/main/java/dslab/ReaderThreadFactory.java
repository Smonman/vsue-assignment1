package dslab;

import dslab.mailbox.DMAPReaderThread;
import dslab.mailbox.DMTPReaderThread;
import dslab.mailbox.lookup.UserLookup;
import dslab.mailbox.storage.Storage;
import dslab.util.Config;

import java.net.Socket;

public final class ReaderThreadFactory {

    private ReaderThreadFactory() {
    }

    public static Thread createReaderThread(final String type,
                                            final Socket socket,
                                            final Config config,
                                            final UserLookup userLookup,
                                            final Storage storage) {
        if (type.equals("dmtp")) {
            return new DMTPReaderThread(socket, config, userLookup, storage);
        } else if (type.equals("dmap")) {
            return new DMAPReaderThread(socket, userLookup, storage);
        } else {
            return null;
        }
    }
}
