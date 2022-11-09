package dslab.protocol.general.instruction.impl;

import dslab.protocol.general.instruction.Instruction;
import dslab.protocol.general.instruction.validator.ArgumentValidator;
import dslab.protocol.general.instruction.validator.exception.InstructionValidationException;

import java.util.List;

/**
 * A base implementation of {@code Instruction}.
 *
 * <p>Provides standard constructors as well as setter for required and
 * dependent instructions. Further, it provides hooks for the handling of
 * instructions and instruction resets, which are both called after the standard
 * implementation of its respective counterpart in the {@code Instruction}
 * interface.
 *
 * @apiNote It is important, that no circular dependencies are constructed via
 * the required instructions or dependent instructions.
 * @see Instruction
 */
public abstract class InstructionBase implements Instruction {

    private List<ArgumentValidator> validators;
    private List<Instruction> requiredInstructions;
    private List<Instruction> dependentInstructions;

    public InstructionBase() {
    }

    public InstructionBase(final List<ArgumentValidator> validators,
                           final List<Instruction> requiredInstructions,
                           final List<Instruction> dependentInstructions) {
        this.validators = validators;
        this.requiredInstructions = requiredInstructions;
        this.dependentInstructions = dependentInstructions;
    }

    public void setRequiredInstructions(
        final List<Instruction> requiredInstructions) {
        this.requiredInstructions = requiredInstructions;
    }

    public void setDependentInstructions(
        final List<Instruction> dependentInstructions) {
        this.dependentInstructions = dependentInstructions;
    }

    protected List<ArgumentValidator> getValidators() {
        return validators;
    }

    public void setValidators(final List<ArgumentValidator> validators) {
        this.validators = validators;
    }

    /**
     * A hook that is called after this handle instruction method.
     *
     * <p>This method should be used to handle instruction specific logic.
     *
     * @param argument the argument of this instruction
     * @return the response tho this instruction
     * @apiNote After this call, the {@code isSet} function of this instruction
     * should reflect the changes, when no error occurs. The response should be
     * set via the {@code OkErrorResponse}, and extended, functionality.
     * @see this#handleInstructionHook(String)
     * @see dslab.protocol.general.OkErrorResponse
     */
    protected abstract String handleInstructionHook(final String argument);


    /**
     * This hook is called after this implementation of the reset method.
     * This should reset this instruction.
     */
    protected abstract void resetHook();

    /**
     * An implementation of the handle instruction method provided by the
     * instruction interface.
     *
     * <p>This implementation checks if any required instructions are not set, and
     * returns their error response. If every required instruction is set, this
     * argument is validated via the given validators next.
     * If any validator throws an exception, the reset method is called and the
     * function returns the special error response of the failed validation.
     *
     * <p>After checking the required instructions and validation the argument the
     * handle instruction hook is called and its return value returned.
     *
     * @param argument the argument to this instruction
     * @return an error response of a missing required instruction, the special
     * error response of a failed validator or the result of the handle
     * instruction hook.
     * @see Instruction#isSet()
     * @see ArgumentValidator
     * @see dslab.protocol.general.SpecialErrorResponse
     * @see this#handleInstructionHook(String)
     */
    @Override
    public String handleInstruction(final String argument) {
        if (requiredInstructions != null) {
            for (Instruction i : requiredInstructions) {
                if (!i.isSet()) {
                    return i.errorResponse();
                }
            }
        }
        if (getValidators() != null) {
            for (ArgumentValidator v : getValidators()) {
                try {
                    v.validateArgument(argument.trim());
                } catch (InstructionValidationException e) {
                    reset();
                    return v.specialErrorResponse();
                }
            }
        }
        return handleInstructionHook(argument);
    }

    /**
     * An implementation of the reset method provided by the instruction
     * interface.
     *
     * <p>This implementation resets every dependent instruction before calling the
     * reset hook.
     */
    @Override
    public void reset() {
        if (dependentInstructions != null) {
            for (Instruction i : dependentInstructions) {
                i.reset();
            }
        }
        resetHook();
    }
}
