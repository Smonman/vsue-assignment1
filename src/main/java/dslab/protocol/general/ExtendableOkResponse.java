package dslab.protocol.general;

/**
 * This interface provides the possibility to extend the default ok response.
 *
 * @see OkErrorResponse
 */
public interface ExtendableOkResponse extends OkErrorResponse {

    /**
     * Ok response extension.
     *
     * <p>This hook is used to extend any implemented ok response. It's return
     * value will be appended to the ok response.
     *
     * @return the ok response extension
     */
    String okResponseExtension();

    /**
     * The default implementation of the ok response in combination with
     * the extension.
     *
     * @return the default ok response with the extension appended
     * @see OkErrorResponse#okResponse()
     */
    @Override
    default String okResponse() {
        return OkErrorResponse.super.okResponse() + " " + okResponseExtension();
    }
}
