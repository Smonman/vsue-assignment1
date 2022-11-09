package dslab.protocol.dmap.instruction.impl;

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
public final class DMAPQuitInstruction extends InstructionBase
    implements ExtendableOkResponse {

    private boolean receivedQuit;

    public DMAPQuitInstruction() {
        this(null, null, null);
    }

    public DMAPQuitInstruction(final List<ArgumentValidator> validators,
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
    public boolean isSet() {
        return receivedQuit;
    }

    @Override
    protected String handleInstructionHook(final String argument) {
        receivedQuit = true;
        return okResponse();
    }

    @Override
    protected void resetHook() {
        receivedQuit = false;
    }
}
