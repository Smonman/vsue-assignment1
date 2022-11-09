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
public final class DMAPDeleteInstruction extends InstructionBase implements
    SpecialErrorResponse {

    private boolean receivedDelete;
    private String id;
    private Hook<Void, String> externalHook;
    private Hook<Boolean, String> existsHook;

    public DMAPDeleteInstruction() {
        this(null, null, null);
    }

    public DMAPDeleteInstruction(final List<ArgumentValidator> validators,
                                 final List<Instruction> requiredInstructions,
                                 final List<Instruction> dependentInstructions
    ) {
        super(validators, requiredInstructions, dependentInstructions);
        this.receivedDelete = false;
        this.id = null;
    }

    public void setExternalHook(final Hook<Void, String> externalHook) {
        this.externalHook = externalHook;
    }

    public void setExistsHook(
        final Hook<Boolean, String> existsHook) {
        this.existsHook = existsHook;
    }

    @Override
    public boolean isSet() {
        return receivedDelete;
    }

    @Override
    public String getValue() {
        return id;
    }

    // precondition for the external hook is that the existence
    // is checked via the exists hook
    @Override
    protected String handleInstructionHook(final String argument) {
        receivedDelete = true;
        id = argument.trim();
        if (!existsHook.hook(id)) {
            return specialErrorResponse(
                String.format("could not find any message with id %s", id));
        }
        externalHook.hook(id);
        return okResponse();
    }

    @Override
    protected void resetHook() {
        receivedDelete = false;
        id = null;
    }
}
