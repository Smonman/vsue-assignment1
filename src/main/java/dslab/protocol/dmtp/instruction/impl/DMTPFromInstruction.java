package dslab.protocol.dmtp.instruction.impl;

import dslab.protocol.general.ExtendableErrorResponse;
import dslab.protocol.general.SpecialErrorResponse;
import dslab.protocol.general.instruction.Instruction;
import dslab.protocol.general.instruction.impl.InstructionBase;
import dslab.protocol.general.instruction.validator.ArgumentValidator;

import java.util.List;

/**
 * A concrete implementation of {@code InstructionBase}.
 *
 * @see InstructionBase
 */
public final class DMTPFromInstruction extends InstructionBase
        implements ExtendableErrorResponse, SpecialErrorResponse {

    private String from;

    public DMTPFromInstruction() {
        this(null, null, null);
    }

    public DMTPFromInstruction(final List<ArgumentValidator> validators,
                               final List<Instruction> requiredInstructions,
                               final List<Instruction> dependentInstructions) {
        super(validators, requiredInstructions, dependentInstructions);
        this.from = null;
    }

    @Override
    public String errorResponseExtension() {
        return "no sender";
    }

    @Override
    public String handleInstructionHook(final String argument) {
        from = argument.trim();
        return okResponse();
    }

    @Override
    public void resetHook() {
        from = null;
    }

    @Override
    public boolean isSet() {
        return from != null;
    }

    @Override
    public String getValue() {
        return from;
    }
}
