package dslab.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Provides default methods to close closeables.
 *
 * @see Closeable
 */
public interface CloseableResource {

    Log LOG = LogFactory.getLog(MethodHandles.lookup().lookupClass());

    /**
     * This method closes the given closeable.
     *
     * <p>If the given closeable is {@code null} this method has no effect.
     * Any io exception that is thrown will be ignored.
     *
     * @param closeable the closable to be closed
     */
    default void closeCloseable(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignore) {
            }
        }
    }

    /**
     * Closes the given executor service.
     *
     * <p>This method closes the given executor service in two phases. First by
     * calling shutdown to reject incoming tasks, and then calling
     * {@code shutdownNow}, if necessary, to cancel any lingering tasks.
     *
     * @param pool {@code ExecutorService} to be shut down
     * @apiNote <a href="https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html">see implementation</a>
     */
    default void shutdownAndAwaitTermination(final ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
                    LOG.error("Pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            LOG.warn("An interrupt occurred", e);
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
