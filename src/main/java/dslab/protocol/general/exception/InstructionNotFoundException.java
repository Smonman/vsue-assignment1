package dslab.protocol.general.exception;

/**
 * An extension of Exception.
 */
public class InstructionNotFoundException extends Exception {
    public InstructionNotFoundException() {
        super();
    }

    public InstructionNotFoundException(final String message) {
        super(message);
    }

    public InstructionNotFoundException(final String message,
                                        final Throwable cause) {
        super(message, cause);
    }

    public InstructionNotFoundException(final Throwable cause) {
        super(cause);
    }
}
