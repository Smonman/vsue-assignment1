package dslab.protocol.general;

/**
 * This interface provides a way to create a specific error response.
 * It uses the standard error response as a basis by default.
 *
 * @see OkErrorResponse#errorResponse()
 */
public interface SpecialErrorResponse extends OkErrorResponse {

    /**
     * A special error response.
     *
     * <p>Creates and returns a specific error message, with the default error
     * response as a basis by default.
     *
     * @param specialErrorMessage the special error message
     * @return the default error response with the special error message
     * appended
     * @see OkErrorResponse#errorResponse()
     */
    default String specialErrorResponse(final String specialErrorMessage) {
        return String.join(" ", OkErrorResponse.super.errorResponse(),
            specialErrorMessage);
    }
}
