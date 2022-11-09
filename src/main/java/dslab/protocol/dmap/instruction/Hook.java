package dslab.protocol.dmap.instruction;

/**
 * This represents a hook.
 *
 * <p>This hook accepts a single parameter.
 *
 * @param <R> the return type
 * @param <P> the parameter type
 */
public interface Hook<R, P> {

    /**
     * The hook method.
     *
     * <p>The hook method that is called.
     *
     * @param p the parameter
     * @return the return value of the hook.
     */
    R hook(P p);
}
