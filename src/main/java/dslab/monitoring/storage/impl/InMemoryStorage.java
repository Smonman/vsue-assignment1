package dslab.monitoring.storage.impl;

import dslab.monitoring.storage.Storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is an implementation of the traffic data storage.
 *
 * <p> The storage is in memory only, nothing will be persisted.
 *
 * @see Storage
 */
public final class InMemoryStorage implements Storage {

    // TODO use atomic int
    private final Map<String, Integer> silo;

    public InMemoryStorage() {
        this.silo = new HashMap<>();
    }

    @Override
    public void increment(String key) {
        if (silo.containsKey(key)) {
            silo.put(key, silo.get(key) + 1);
        } else {
            silo.put(key, 1);
        }
    }

    @Override
    public List<String> dump() {
        return silo
                .keySet()
                .stream()
                .map(k -> String.format("%s %d", k, silo.get(k)))
                .collect(Collectors.toList());
    }
}
