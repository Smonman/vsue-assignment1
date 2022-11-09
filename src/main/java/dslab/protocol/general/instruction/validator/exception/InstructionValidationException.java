package dslab.protocol.general.instruction.validator.exception;

/**
 * An extension of Exception.
 *
 * @see Exception
 */
public class InstructionValidationException extends Exception {

    public InstructionValidationException() {
        super();
    }

    public InstructionValidationException(final String message) {
        super(message);
    }

    public InstructionValidationException(final String message,
                                          final Throwable cause) {
        super(message, cause);
    }

    public InstructionValidationException(final Throwable cause) {
        super(cause);
    }
}
