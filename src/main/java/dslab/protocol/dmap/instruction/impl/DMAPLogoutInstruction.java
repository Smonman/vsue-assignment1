package dslab.protocol.dmap.instruction.impl;

import dslab.protocol.general.instruction.Instruction;
import dslab.protocol.general.instruction.impl.InstructionBase;
import dslab.protocol.general.instruction.validator.ArgumentValidator;

import java.util.List;

/**
 * A concrete implementation of {@code InstructionBase}.
 *
 * @see InstructionBase
 */
public final class DMAPLogoutInstruction extends InstructionBase {

    private boolean receivedLogout;

    public DMAPLogoutInstruction() {
        this(null, null, null);
    }

    public DMAPLogoutInstruction(final List<ArgumentValidator> validators,
                                 final List<Instruction> requiredInstructions,
                                 final List<Instruction> dependentInstructions
    ) {
        super(validators, requiredInstructions, dependentInstructions);
        this.receivedLogout = false;
    }

    @Override
    public boolean isSet() {
        return receivedLogout;
    }

    @Override
    protected String handleInstructionHook(final String argument) {
        reset();
        this.receivedLogout = true;
        return okResponse();
    }

    @Override
    protected void resetHook() {
        receivedLogout = false;
    }
}
