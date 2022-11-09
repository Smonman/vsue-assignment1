package dslab.protocol.general.instruction.validator;

import dslab.protocol.general.SpecialErrorResponse;
import dslab.protocol.general.instruction.validator.exception.InstructionValidationException;

/**
 * A representation of an argument validator.
 * An extension of {@code SpecialErrorResponse}.
 *
 * <p>This provides methods for an argument validator for the DMTP or DMAP
 * protocols. It does not validate the command itself but only the argument of
 * the command.
 *
 * @see SpecialErrorResponse
 */
public interface ArgumentValidator extends SpecialErrorResponse {

    /**
     * Validates the argument.
     *
     * @param argument the argument of the instruction.
     * @throws InstructionValidationException when the validation fails
     */
    void validateArgument(String argument)
        throws InstructionValidationException;

    /**
     * A special error response hook.
     *
     * <p>This sets the error message for the {@code specialErrorResponse} of
     * the special error response interface.
     *
     * @return the special error response message
     * @see SpecialErrorResponse
     */
    String specialErrorResponseHook();

    /**
     * The default implementation of the special error response method in this.
     *
     * <p>Calls the special error response method with the special error
     * response hook as the error message.
     *
     * @return the special error message
     */
    default String specialErrorResponse() {
        return specialErrorResponse(specialErrorResponseHook());
    }
}
