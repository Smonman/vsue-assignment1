package dslab.protocol.general.instruction.validator.impl;

import dslab.protocol.general.instruction.validator.exception.InstructionValidationException;

/**
 * A concrete implementation of {@code ArgumentValidatorBase}.
 *
 * <p>This validator checks whether an argument was supplied. This will only
 * consider an argument as missing if it has a length of 0, not if it is blank.
 */
public final class ArgumentRequiredValidator extends ArgumentValidatorBase {

    /**
     * The empty constructor sets the error prefix to {@code null}.
     */
    public ArgumentRequiredValidator() {
        super();
    }

    /**
     * @param errorPrefix the prefix for the error
     */
    public ArgumentRequiredValidator(final String errorPrefix) {
        super(errorPrefix);
    }

    @Override
    public void validateArgument(final String argument)
        throws InstructionValidationException {
        if (argument.isEmpty()) {
            throw new InstructionValidationException();
        }
    }

    @Override
    protected String errorHook() {
        return "requires an argument";
    }
}
