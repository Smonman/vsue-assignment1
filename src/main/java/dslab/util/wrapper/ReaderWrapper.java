package dslab.util.wrapper;

import java.io.BufferedReader;
import java.io.IOException;

public interface ReaderWrapper {
    default String readLineFromBufferedReader(final BufferedReader br)
        throws IOException {
        return br.readLine();
    }
}
