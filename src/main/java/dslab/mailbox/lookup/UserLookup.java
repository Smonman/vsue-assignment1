package dslab.mailbox.lookup;

import dslab.util.lookup.impl.LookupBase;

import java.util.HashMap;
import java.util.Map;

/**
 * A concrete implementation of {@code LookupBase}.
 *
 * <p>{@code UserLookup} provides the possibility to look up users of a specific
 * server persisted in the associated users properties file.
 */
public final class UserLookup extends LookupBase {
    private Map<String, String> user;

    public UserLookup(final String domain) {
        super(String.format("users-%s.properties", domain.replace('.', '-')));
    }

    @Override
    protected void init() {
        user = new HashMap<>();
    }

    @Override
    protected void load() {
        getConfig()
            .listKeys()
            .forEach(u -> user.put(u, getConfig().getString(u)));
    }

    @Override
    public boolean containsKey(final String key) {
        return user.containsKey(key);
    }

    public String get(final String key) {
        return user.get(key);
    }
}
