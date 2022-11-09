package dslab.protocol.general;

/**
 * This interface provides hooks for the default ok and error
 * response of the DMTP or DMAP protocol.
 */
public interface OkErrorResponse {

    /**
     * Default ok response.
     *
     * <p>Overwrite this to change the default ok response.
     *
     * @return the default ok response.
     */
    default String okResponse() {
        return "ok";
    }

    /**
     * Default error response.
     *
     * <p>Overwrite this to change the default error response.
     *
     * @return the default error response.
     */
    default String errorResponse() {
        return "error";
    }
}
