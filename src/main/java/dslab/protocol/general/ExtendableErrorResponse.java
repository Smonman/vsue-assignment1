package dslab.protocol.general;

/**
 * This interface provides the possibility to extend the default error response.
 *
 * @see OkErrorResponse
 */
public interface ExtendableErrorResponse extends OkErrorResponse {

    /**
     * Error response extension.
     *
     * <p>This hook is used to extend any implemented error response. It's return
     * value will be appended to the error response.
     *
     * @return the error response extension
     */
    String errorResponseExtension();

    /**
     * The default implementation of the error response in combination with
     * the extension.
     *
     * @return the default error response with the extension appended
     * @see OkErrorResponse#errorResponse()
     */
    @Override
    default String errorResponse() {
        return String.join(" ", OkErrorResponse.super.errorResponse(),
            errorResponseExtension());
    }
}
