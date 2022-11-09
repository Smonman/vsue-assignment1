package dslab.protocol.dmtp.impl;

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
public final class DMTPDataInstruction extends InstructionBase
    implements ExtendableErrorResponse {

    private String data;

    public DMTPDataInstruction() {
        this(null, null, null);
    }

    public DMTPDataInstruction(final List<ArgumentValidator> validators,
                               final List<Instruction> requiredInstructions,
                               final List<Instruction> dependentInstructions) {
        super(validators, requiredInstructions, dependentInstructions);
        this.data = null;
    }

    @Override
    public String errorResponseExtension() {
        return "no data";
    }

    @Override
    public String handleInstructionHook(final String argument) {
        data = argument.trim();
        return okResponse();
    }

    @Override
    public void resetHook() {
        data = null;
    }

    @Override
    public boolean isSet() {
        return data != null;
    }

    @Override
    public String getValue() {
        return data;
    }
}
