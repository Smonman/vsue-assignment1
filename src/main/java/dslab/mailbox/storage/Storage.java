package dslab.mailbox.storage;

import dslab.mailbox.storage.key.Key;
import dslab.protocol.dmtp.message.DMTPMessage;

import java.util.List;

/**
 * This represents a storage for DMTP messages.
 */
public interface Storage {

    /**
     * Stores a message in this.
     *
     * <p>After this call the message is stored inside the storage and can be
     * accessed via {@code get} and {@code contains} will return true for the
     * key associated with the message.
     *
     * @param message the message to be stored
     */
    void store(DMTPMessage message);

    /**
     * Gets the id message pair associated with the key.
     *
     * <p>This method does not change anything in the storage.
     *
     * @param key the key of the value to be returned
     * @return the id message pair of the given key
     */
    IdMessagePair get(Key key);

    /**
     * Removes the message associated with the given key from the storage.
     *
     * <p>After this call the message cannot be retrieved in any form. Calling
     * remove on the same key will only change the state of the storage the
     * first time.
     *
     * @param key the key of the message to be removed
     */
    void remove(Key key);

    /**
     * Returns all id message pairs associated with a username.
     *
     * @param username the username of the messages that should be returned
     * @return a list of all id message pairs that are associated with the
     * username
     */
    List<IdMessagePair> getAll(String username);

    /**
     * Returns whether a given key is present in the storage.
     *
     * @param key the key to be tested
     * @return true if the given key is present in the storage, false otherwise
     */
    boolean contains(Key key);

    /**
     * This is an id message pair.
     */
    class IdMessagePair {
        private final int id;
        private final DMTPMessage message;

        public IdMessagePair(final int id,
                             final DMTPMessage message) {
            this.id = id;
            this.message = message;
        }

        public int getId() {
            return id;
        }

        public DMTPMessage getMessage() {
            return message;
        }
    }
}
