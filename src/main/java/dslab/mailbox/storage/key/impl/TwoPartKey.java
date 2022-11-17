package dslab.mailbox.storage.key.impl;

import dslab.protocol.dmtp.message.DMTPMessage;
import dslab.util.parser.AddressParser;

import java.util.Objects;

/**
 * A concrete implementation if {@code KeyBase}.
 *
 * <p>This key is a composition of two values.
 *
 * @see KeyBase
 */
public final class TwoPartKey extends KeyBase {

    private final int id;
    private final String username;

    private TwoPartKey(final int id,
                       final String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * Creates a new {@code TwoPartKey} from an id and a message.
     *
     * @param id      the id of the message
     * @param message the message
     * @return a new {@code TwoPartKey}
     */
    public static TwoPartKey of(final int id, final DMTPMessage message) {
        String username = AddressParser.getUsername(message.getPrimaryTo());
        return TwoPartKey.of(id, username);
    }

    /**
     * Creates a new {@code TwoPartKey} from an id and a username.
     *
     * @param id       the id of the message
     * @param username the username of the owner of the message
     * @return a new {@code TwoPartKey}
     */
    public static TwoPartKey of(final int id, final String username) {
        return new TwoPartKey(id, username);
    }

    @Override
    public String getKey() {
        return String.format("%d %s", id, username);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TwoPartKey that = (TwoPartKey) o;
        return id == that.id && Objects.equals(username, that.username);
    }
}
