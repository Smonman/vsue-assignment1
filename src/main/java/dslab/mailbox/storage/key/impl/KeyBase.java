package dslab.mailbox.storage.key.impl;

import dslab.mailbox.storage.key.Key;

/**
 * A base implementation of the key.
 *
 * <p>The key needs to be comparable.
 *
 * @see Key
 */
public abstract class KeyBase implements Key {

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}
