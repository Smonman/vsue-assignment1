package dslab.transfer.lookup;

import dslab.util.lookup.impl.LookupBase;

import java.util.HashMap;
import java.util.Map;

/**
 * A concrete implementation of {@code LookupBase}.
 *
 * <p>{@code DomainLookup} provides the possibility to look up domains persisted
 * in the {@code domains.properties} properties file.
 */
public final class DomainLookup extends LookupBase {
    private Map<String, DomainPair> domains;

    public DomainLookup() {
        super("domains.properties");
    }

    @Override
    protected void init() {
        domains = new HashMap<>();
    }

    @Override
    protected void load() {
        getConfig()
            .listKeys()
            .forEach(
                k -> domains.put(k, DomainPair.of(getConfig().getString(k))));
    }

    @Override
    public boolean containsKey(final String key) {
        return domains.containsKey(key);
    }

    public String getAddress(final String domain) {
        return domains.get(domain).address;
    }

    public int getPort(final String domain) {
        return domains.get(domain).port;
    }

    static class DomainPair {
        private final String address;
        private final int port;

        private DomainPair(final String address,
                           final int port) {
            this.address = address;
            this.port = port;
        }

        static DomainPair of(final String addressPortPair) {
            String[] parts = addressPortPair.split(":");
            return new DomainPair(parts[0], Integer.parseInt(parts[1]));
        }
    }
}
