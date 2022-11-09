package dslab.mailbox.storage.impl;

import dslab.mailbox.storage.Storage;
import dslab.mailbox.storage.key.Key;
import dslab.mailbox.storage.key.impl.TwoPartKey;
import dslab.protocol.dmtp.message.DMTPMessage;
import dslab.util.AddressParser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A concrete implementation of {@code Storage}.
 *
 * <p>This implementation is thread save.
 * The storage is in memory only, nothing will be persisted.
 *
 * @see Storage
 */
public final class InMemoryStorage implements Storage {

    private final Map<Key, IdMessagePair> silo;

    public InMemoryStorage() {
        this.silo = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void store(final DMTPMessage message) {
        int id = getNextId(message);
        silo.put(TwoPartKey.of(id, message),
            new IdMessagePair(id, message));
    }

    @Override
    public IdMessagePair get(final Key key) {
        return silo.get(key);
    }

    @Override
    public void remove(final Key key) {
        silo.remove(key);
    }

    @Override
    public synchronized List<IdMessagePair> getAll(final String username) {
        return getAllKeysOfUser(username)
            .stream()
            .map(silo::get)
            .collect(Collectors.toList());
    }

    @Override
    public boolean contains(final Key key) {
        return silo.containsKey(key);
    }

    private int getNextId(final DMTPMessage message) {
        String username = AddressParser.getUsername(message.getTo());
        return silo
            .keySet()
            .stream()
            .filter(k -> ((TwoPartKey) k).getUsername().equals(username))
            .map(k -> ((TwoPartKey) k).getId())
            .reduce(0, Integer::max) + 1;
    }

    private List<Key> getAllKeysOfUser(final String username) {
        return silo
            .keySet()
            .stream()
            .filter(k -> ((TwoPartKey) k).getUsername().equals(username))
            .collect(Collectors.toList());
    }
}
