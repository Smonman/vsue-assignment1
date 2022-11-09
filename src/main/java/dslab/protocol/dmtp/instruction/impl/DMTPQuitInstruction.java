package dslab.protocol.dmtp.instruction.impl;

import dslab.protocol.general.ExtendableErrorResponse;
import dslab.protocol.general.ExtendableOkResponse;
import dslab.protocol.general.instruction.Instruction;
import dslab.protocol.general.instruction.impl.InstructionBase;
import dslab.protocol.general.instruction.validator.ArgumentValidator;

import java.util.List;

/**
 * A concrete implementation of {@code InstructionBase}.
 *
 * @see InstructionBase
 */
public final class DMTPQuitInstruction extends InstructionBase
    implements ExtendableOkResponse, ExtendableErrorResponse {

    private boolean receivedQuit;

    public DMTPQuitInstruction() {
        this(null, null, null);
    }

    public DMTPQuitInstruction(final List<ArgumentValidator> validators,
                               final List<Instruction> requiredInstructions,
                               final List<Instruction> dependentInstructions) {
        super(validators, requiredInstructions, dependentInstructions);
        this.receivedQuit = false;
    }

    @Override
    public String okResponseExtension() {
        return "bye";
    }

    @Override
    public String errorResponseExtension() {
        return "quit takes no argument";
    }

    @Override
    public String handleInstructionHook(final String argument) {
        receivedQuit = true;
        return okResponse();
    }

    @Override
    public void resetHook() {
        receivedQuit = false;
    }

    @Override
    public boolean isSet() {
        return receivedQuit;
    }
}
