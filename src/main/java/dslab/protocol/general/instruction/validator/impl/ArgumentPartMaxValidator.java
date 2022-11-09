package dslab.protocol.general.instruction.validator.impl;

import dslab.protocol.general.instruction.validator.exception.InstructionValidationException;

/**
 * A concrete implementation of {@code ArgumentValidatorBase}.
 *
 * <p>This validator checks whether the given argument has more than
 * {@code maxParts} parts. The argument is split via a given delimiter.
 */
public final class ArgumentPartMaxValidator extends ArgumentValidatorBase {

    private final String delimiter;
    private final int maxParts;

    /**
     * @param delimiter the parts delimiter
     * @param maxParts  has to be larger or equal to 1
     */
    public ArgumentPartMaxValidator(final String delimiter,
                                    final int maxParts) {
        this(null, delimiter, maxParts);
    }

    /**
     * @param errorPrefix the prefix for the error
     * @param delimiter   the parts delimiter
     * @param maxParts    has to be larger or equal to 1
     */
    public ArgumentPartMaxValidator(final String errorPrefix,
                                    final String delimiter,
                                    final int maxParts) {
        super(errorPrefix);
        this.delimiter = delimiter;
        this.maxParts = maxParts;
    }

    @Override
    public void validateArgument(final String argument)
        throws InstructionValidationException {
        String[] parts = argument.trim().split(delimiter);
        if (parts.length > maxParts) {
            throw new InstructionValidationException();
        }
    }

    @Override
    protected String errorHook() {
        return String.format("does not allow more than %d argument part(s)",
            maxParts);
    }
}
