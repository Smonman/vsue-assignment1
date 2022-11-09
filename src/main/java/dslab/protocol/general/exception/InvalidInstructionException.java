package dslab.protocol.general.exception;

/**
 * An extension of Exception.
 *
 * @see Exception
 */
public class InvalidInstructionException extends Exception {
    public InvalidInstructionException() {
        super();
    }

    public InvalidInstructionException(final String message) {
        super(message);
    }

    public InvalidInstructionException(final String message,
                                       final Throwable cause) {
        super(message, cause);
    }

    public InvalidInstructionException(final Throwable cause) {
        super(cause);
    }
}
