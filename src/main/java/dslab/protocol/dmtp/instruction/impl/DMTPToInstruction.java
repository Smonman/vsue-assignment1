package dslab.protocol.dmtp.instruction.impl;

import dslab.protocol.dmap.instruction.Hook;
import dslab.protocol.general.ExtendableErrorResponse;
import dslab.protocol.general.ExtendableOkResponse;
import dslab.protocol.general.SpecialErrorResponse;
import dslab.protocol.general.instruction.Instruction;
import dslab.protocol.general.instruction.impl.InstructionBase;
import dslab.protocol.general.instruction.validator.ArgumentValidator;
import dslab.util.AddressParser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A concrete implementation of {@code InstructionBase}.
 *
 * @see InstructionBase
 */
public final class DMTPToInstruction extends InstructionBase
        implements ExtendableOkResponse, ExtendableErrorResponse,
        SpecialErrorResponse {

    private List<String> to;
    private Hook<Boolean, String> acceptHook;
    private Hook<Boolean, String> isKnownHook;

    public DMTPToInstruction() {
        this(null, null, null);
    }

    public DMTPToInstruction(final List<ArgumentValidator> validators,
                             final List<Instruction> requiredInstructions,
                             final List<Instruction> dependentInstructions) {
        super(validators, requiredInstructions, dependentInstructions);
        this.to = null;
    }

    public void setAcceptHook(
            final Hook<Boolean, String> acceptHook) {
        this.acceptHook = acceptHook;
    }

    public void setIsKnownHook(
            final Hook<Boolean, String> isKnownHook) {
        this.isKnownHook = isKnownHook;
    }

    @Override
    public String okResponseExtension() {
        return Integer.toString(to.size());
    }

    @Override
    public String errorResponseExtension() {
        return "no recipient(s)";
    }

    @Override
    public String handleInstructionHook(final String argument) {
        String[] parts = argument.trim().split(",");
        to = Stream.of(parts)
                .filter(p -> acceptHook.hook(p))
                .collect(Collectors.toList());
        if (to.size() == 0) {
            reset();
            return errorResponse();
        }
        List<String> unknownTo = to.stream()
                .filter(p -> !isKnownHook.hook(p))
                .map(AddressParser::getUsername)
                .collect(Collectors.toList());
        if (unknownTo.size() > 0) {
            reset();
            return specialErrorResponse(String.format("unknown recipient(s) %s",
                    String.join(", ", unknownTo)));
        }
        return okResponse();
    }

    @Override
    public void resetHook() {
        to = null;
    }

    @Override
    public boolean isSet() {
        return to != null;
    }

    @Override
    public String getValue() {
        return to
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
