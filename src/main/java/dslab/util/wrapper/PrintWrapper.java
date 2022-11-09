package dslab.util.wrapper;

import java.io.PrintWriter;

public interface PrintWrapper {
    default void printlnToPrintWriter(final PrintWriter pw, final String msg) {
        pw.println(msg);
        pw.flush();
    }
}
