package dslab.util.lookup.impl;

import dslab.util.Config;
import dslab.util.lookup.Lookup;

/**
 * A base implementation of Lookup.
 *
 * <p>This implementation requires a config that it uses as its data source.
 * It provides a constructor with an {@code init} and {@code load} hook, which
 * are called in order only once at the end of the constructor.
 * {@code init} init should be thought of as a constructor for concrete
 * implementations.
 */
public abstract class LookupBase implements Lookup {

    private final String configName;
    private final Config config;

    public LookupBase(final String configName) {
        this.configName = configName;
        this.config = new Config(getConfigName());
        init();
        load();
    }

    protected String getConfigName() {
        return configName;
    }

    protected Config getConfig() {
        return config;
    }

    /**
     * A constructor hook. Called once after this constructor.
     */
    protected abstract void init();

    /**
     * A load hook. Called once after {@code init}.
     *
     * @see LookupBase#init()
     */
    protected abstract void load();
}
