package dslab.protocol.dmap.instruction.impl;

import dslab.protocol.dmap.instruction.Hook;
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
public final class DMAPLoginInstruction extends InstructionBase
    implements ExtendableErrorResponse, SpecialErrorResponse {

    private boolean receivedLogin;
    private Hook<Boolean, String> externalHook;

    public DMAPLoginInstruction() {
        this(null, null, null);
    }

    public DMAPLoginInstruction(final List<ArgumentValidator> validators,
                                final List<Instruction> requiredInstructions,
                                final List<Instruction> dependentInstructions) {
        super(validators, requiredInstructions, dependentInstructions);
        this.receivedLogin = false;
    }

    public void setExternalHook(
        final Hook<Boolean, String> externalHook) {
        this.externalHook = externalHook;
    }

    @Override
    public String errorResponseExtension() {
        return "not logged in";
    }

    @Override
    public boolean isSet() {
        return receivedLogin;
    }

    @Override
    protected String handleInstructionHook(final String argument) {
        reset();
        if (externalHook.hook(argument)) {
            receivedLogin = true;
            return okResponse();
        }
        return specialErrorResponse("username or password wrong");
    }

    @Override
    protected void resetHook() {
        this.receivedLogin = false;
    }
}
