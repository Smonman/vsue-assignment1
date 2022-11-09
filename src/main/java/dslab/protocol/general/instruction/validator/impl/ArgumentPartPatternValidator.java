package dslab.protocol.general.instruction.validator.impl;

import dslab.protocol.general.instruction.validator.exception.InstructionValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A concrete implementation of {@code ArgumentValidatorBase}.
 *
 * <p>This validator checks whether all argument parts are compliant with the
 * given regex pattern.
 */
public final class ArgumentPartPatternValidator extends ArgumentValidatorBase {

    private final Pattern pattern;
    private final String delimiter;

    /**
     * @param pattern   the regex pattern every argument part has match
     * @param delimiter the delimiter to find the argument parts
     */
    public ArgumentPartPatternValidator(final String pattern,
                                        final String delimiter) {
        this(null, pattern, delimiter);
    }

    /**
     * @param errorPrefix the prefix for the error
     * @param pattern     the regex pattern every argument part has match
     * @param delimiter   the delimiter to find the argument parts
     */
    public ArgumentPartPatternValidator(final String errorPrefix,
                                        final String pattern,
                                        final String delimiter) {
        super(errorPrefix);
        this.pattern = Pattern.compile(pattern);
        this.delimiter = delimiter;
    }

    @Override
    public void validateArgument(final String argument)
        throws InstructionValidationException {
        String[] parts = argument.trim().split(delimiter);
        for (String p : parts) {
            Matcher matcher = pattern.matcher(p);
            if (!matcher.matches()) {
                throw new InstructionValidationException();
            }
        }
    }

    @Override
    protected String errorHook() {
        return "not a valid argument";
    }
}
