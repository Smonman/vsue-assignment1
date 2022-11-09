package dslab.util.lookup;

/**
 * Lookup provides methods to look up specific values bound
 * to string keys.
 */
public interface Lookup {
    /**
     * This method returns true if the given key is contained in this
     * Lookup object.
     *
     * <p>{@code key} must not be null.
     *
     * @param key the key to be searched for
     * @return true if this contains the key, false otherwise
     */
    boolean containsKey(String key);
}
