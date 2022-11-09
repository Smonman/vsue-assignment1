package dslab.protocol.general.instruction.validator.impl;

import dslab.protocol.general.instruction.validator.exception.InstructionValidationException;

/**
 * A concrete implementation of {@code ArgumentValidatorBase}.
 *
 * <p>This validator checks whether the given argument has less than
 * {@code minParts} parts. The argument is split via a given delimiter.
 */
public final class ArgumentPartMinValidator extends ArgumentValidatorBase {

    private final String delimiter;
    private final int minParts;

    /**
     * @param delimiter the parts delimiter
     * @param minParts  has to be larger or equal to 1
     */
    public ArgumentPartMinValidator(final String delimiter,
                                    final int minParts) {
        this(null, delimiter, minParts);
    }

    /**
     * @param errorPrefix the prefix for the error
     * @param delimiter   the parts delimiter
     * @param minParts    has to be larger or equal to 1
     */
    public ArgumentPartMinValidator(final String errorPrefix,
                                    final String delimiter,
                                    final int minParts) {
        super(errorPrefix);
        this.delimiter = delimiter;
        this.minParts = minParts;
    }

    @Override
    public void validateArgument(final String argument)
        throws InstructionValidationException {
        String[] parts = argument.trim().split(delimiter);
        if (parts.length < minParts) {
            throw new InstructionValidationException();
        }
    }

    @Override
    protected String errorHook() {
        return String.format("does not allow less than %d argument part(2)",
            minParts);
    }
}
