package dslab.protocol.dmtp.impl;

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
public final class DMTPSendInstruction extends InstructionBase
    implements ExtendableErrorResponse, SpecialErrorResponse {

    private boolean receivedSend;

    public DMTPSendInstruction() {
        this(null, null, null);
    }

    public DMTPSendInstruction(final List<ArgumentValidator> validators,
                               final List<Instruction> requiredInstructions,
                               final List<Instruction> dependentInstructions) {
        super(validators, requiredInstructions, dependentInstructions);
        this.receivedSend = false;
    }

    @Override
    public String errorResponseExtension() {
        return "no send";
    }

    @Override
    public String handleInstructionHook(final String argument) {
        receivedSend = true;
        return okResponse();
    }

    @Override
    public void resetHook() {
        receivedSend = false;
    }

    @Override
    public boolean isSet() {
        return receivedSend;
    }
}
