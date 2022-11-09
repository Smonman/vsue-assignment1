package dslab.protocol.dmtp.instruction.impl;

import dslab.protocol.general.ExtendableErrorResponse;
import dslab.protocol.general.instruction.Instruction;
import dslab.protocol.general.instruction.impl.InstructionBase;
import dslab.protocol.general.instruction.validator.ArgumentValidator;

import java.util.List;

/**
 * A concrete implementation of {@code InstructionBase}.
 *
 * @see InstructionBase
 */
public final class DMTPBeginInstruction extends InstructionBase
    implements ExtendableErrorResponse {

    private Boolean receivedBegin;

    public DMTPBeginInstruction() {
        this(null, null, null);
    }

    public DMTPBeginInstruction(final List<ArgumentValidator> validators,
                                final List<Instruction> requiredInstructions,
                                final List<Instruction> dependentInstructions) {
        super(validators, requiredInstructions, dependentInstructions);
        this.receivedBegin = false;
    }

    @Override
    public String errorResponseExtension() {
        return "no begin";
    }

    @Override
    public String handleInstructionHook(final String argument) {
        reset();
        receivedBegin = true;
        return okResponse();
    }

    @Override
    public void resetHook() {
        this.receivedBegin = false;
    }

    @Override
    public boolean isSet() {
        return receivedBegin;
    }
}
