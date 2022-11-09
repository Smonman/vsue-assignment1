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
public final class DMTPSubjectInstruction extends InstructionBase
    implements ExtendableErrorResponse {

    private String subject;

    public DMTPSubjectInstruction() {
        this(null, null, null);
    }

    public DMTPSubjectInstruction(final List<ArgumentValidator> validators,
                                  final List<Instruction> requiredInstructions,
                                  final List<Instruction> dependentInstructions) {
        super(validators, requiredInstructions, dependentInstructions);
        this.subject = null;
    }

    @Override
    public String errorResponseExtension() {
        return "no subject";
    }

    @Override
    public String handleInstructionHook(final String argument) {
        subject = argument.trim();
        return okResponse();
    }

    @Override
    public void resetHook() {
        subject = null;
    }

    @Override
    public boolean isSet() {
        return subject != null;
    }

    @Override
    public String getValue() {
        return subject;
    }
}
