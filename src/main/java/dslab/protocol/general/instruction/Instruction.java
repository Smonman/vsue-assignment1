package dslab.protocol.general.instruction;

import dslab.protocol.general.OkErrorResponse;

/**
 * A representation of an instruction of the DMTP or DMAP protocol.
 * This is an extension of the {@code OkErrorResponse}.
 *
 * <p>It provides methods to work with an instruction.
 *
 * @see OkErrorResponse
 */
public interface Instruction extends OkErrorResponse {

    /**
     * This method handles this instruction with the given argument.
     *
     * <p>The argument must not be {@code null}. The response should be done via
     * the {@code okResponse} or {@code errorResponse}, which can be overwritten
     * if needed.
     *
     * @param argument the argument to this instruction
     * @return the response to this instruction
     * @apiNote This method should not validate the argument. This can be done
     * with a custom validator.
     * @see dslab.protocol.general.instruction.validator.ArgumentValidator
     * @see OkErrorResponse
     */
    String handleInstruction(final String argument);

    /**
     * Returns if this instruction is set.
     *
     * @return true if this instruction is set.
     */
    boolean isSet();

    /**
     * Returns the value of this instruction.
     *
     * <p>This is only defined behaviour if called after {@code handleInstruction}.
     *
     * @return the value of this instruction
     * @apiNote the default return value is {@code null} if not overwritten.
     */
    default String getValue() {
        return null;
    }

    /**
     * Resets this instruction.
     */
    void reset();
}
