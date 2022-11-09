package dslab.protocol.general.instruction.validator.impl;

import dslab.protocol.general.instruction.validator.exception.InstructionValidationException;

/**
 * A concrete implementation of {@code ArgumentValidatorBase}.
 *
 * <p>This validator checks whether the given argument is of type int.
 */
public final class ArgumentIntegerValidator extends ArgumentValidatorBase {

    /**
     * The empty constructor sets the error prefix to {@code null}.
     */
    public ArgumentIntegerValidator() {
        this(null);
    }

    /**
     * @param errorPrefix the prefix for the error
     */
    public ArgumentIntegerValidator(final String errorPrefix) {
        super(errorPrefix);
    }

    @Override
    public void validateArgument(final String argument)
        throws InstructionValidationException {
        try {
            Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new InstructionValidationException(e);
        }
    }

    @Override
    protected String errorHook() {
        return "requires the argument to be of type integer";
    }
}
