package dslab.monitoring.storage;

import java.util.List;

/**
 * This represents a storage for traffic data.
 */
public interface Storage {

    /**
     * Increments the value of the specific key.
     *
     * <p>If no key is present, initiate the key with value 1.
     *
     * @param key the key
     */
    void increment(String key);

    /**
     * Gets the complete content of the storage.
     *
     * @return a list of every key value pair of this storage.
     */
    List<String> dump();
}
