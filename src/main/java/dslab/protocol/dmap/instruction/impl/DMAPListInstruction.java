package dslab.protocol.dmap.instruction.impl;

import dslab.protocol.dmap.instruction.Hook;
import dslab.protocol.general.instruction.Instruction;
import dslab.protocol.general.instruction.impl.InstructionBase;
import dslab.protocol.general.instruction.validator.ArgumentValidator;

import java.util.List;

/**
 * A concrete implementation of {@code InstructionBase}.
 *
 * @see InstructionBase
 */
public final class DMAPListInstruction extends InstructionBase {

    private boolean receivedList;
    private Hook<String, String> externalHook;

    public DMAPListInstruction() {
        this(null, null, null);
    }

    public DMAPListInstruction(final List<ArgumentValidator> validators,
                               final List<Instruction> requiredInstructions,
                               final List<Instruction> dependentInstructions) {
        super(validators, requiredInstructions, dependentInstructions);
        this.receivedList = false;
    }

    public void setExternalHook(final Hook<String, String> externalHook) {
        this.externalHook = externalHook;
    }

    @Override
    public boolean isSet() {
        return receivedList;
    }

    @Override
    protected String handleInstructionHook(final String argument) {
        receivedList = true;
        return externalHook.hook(argument);
    }

    @Override
    protected void resetHook() {
        receivedList = false;
    }
}
