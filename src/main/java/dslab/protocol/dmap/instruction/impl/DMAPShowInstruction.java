package dslab.protocol.dmap.instruction.impl;

import dslab.protocol.dmap.instruction.Hook;
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
public final class DMAPShowInstruction extends InstructionBase implements
    SpecialErrorResponse {

    private boolean receivedShow;
    private Hook<String, String> externalHook;
    private Hook<Boolean, String> existsHook;

    public DMAPShowInstruction() {
        this(null, null, null);
    }

    public DMAPShowInstruction(final List<ArgumentValidator> validators,
                               final List<Instruction> requiredInstructions,
                               final List<Instruction> dependentInstructions) {
        super(validators, requiredInstructions, dependentInstructions);
        this.receivedShow = false;
    }

    public void setExternalHook(final Hook<String, String> externalHook) {
        this.externalHook = externalHook;
    }

    public void setExistsHook(
        Hook<Boolean, String> existsHook) {
        this.existsHook = existsHook;
    }

    @Override
    public boolean isSet() {
        return receivedShow;
    }

    // precondition for the external hook is that the existence
    // is checked via the exists hook
    @Override
    protected String handleInstructionHook(final String argument) {
        receivedShow = true;
        String id = argument.trim();
        if (!existsHook.hook(id)) {
            return specialErrorResponse(
                String.format("could not find any message with id %s", id));
        }
        return externalHook.hook(id);
    }

    @Override
    protected void resetHook() {
        receivedShow = false;
    }
}
