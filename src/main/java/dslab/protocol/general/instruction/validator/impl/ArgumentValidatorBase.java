package dslab.protocol.general.instruction.validator.impl;

import dslab.protocol.general.instruction.validator.ArgumentValidator;

/**
 * A base implementation of the argument validator.
 *
 * <p>This implementation provides an error prefix which is prepended before the
 * error hook.
 *
 * @apiNote the error prefix should be equal to the instruction name.
 * @see ArgumentValidator
 */
public abstract class ArgumentValidatorBase implements ArgumentValidator {

    private final String errorPrefix;

    public ArgumentValidatorBase() {
        this(null);
    }

    public ArgumentValidatorBase(final String errorPrefix) {
        this.errorPrefix =
            errorPrefix == null ? "" : String.format("%s ", errorPrefix.trim());
    }

    /**
     * An implementation of the special error response hook of the argument
     * validator interface.
     *
     * <p>This implementation prepends an error prefix before calling the error
     * hook.
     *
     * @return the error hook with the error prefix prepended.
     * @see this#errorHook()
     * @see ArgumentValidator#specialErrorResponseHook()
     */
    @Override
    public String specialErrorResponseHook() {
        return String.join("", errorPrefix, errorHook());
    }

    /**
     * An error hook.
     *
     * <p>This is a hook for the special error message. The return value must
     * not be {@code null}.
     *
     * @return the special error message
     */
    protected abstract String errorHook();
}
