package dslab.protocol.dmtp.instruction.map;

import dslab.protocol.dmap.instruction.Hook;
import dslab.protocol.dmtp.instruction.impl.DMTPBeginInstruction;
import dslab.protocol.dmtp.instruction.impl.DMTPDataInstruction;
import dslab.protocol.dmtp.instruction.impl.DMTPFromInstruction;
import dslab.protocol.dmtp.instruction.impl.DMTPQuitInstruction;
import dslab.protocol.dmtp.instruction.impl.DMTPSendInstruction;
import dslab.protocol.dmtp.instruction.impl.DMTPSubjectInstruction;
import dslab.protocol.dmtp.instruction.impl.DMTPToInstruction;
import dslab.protocol.general.instruction.map.impl.InstructionMapBase;
import dslab.protocol.general.instruction.validator.impl.ArgumentPartMaxValidator;
import dslab.protocol.general.instruction.validator.impl.ArgumentPartMinValidator;
import dslab.protocol.general.instruction.validator.impl.ArgumentPartPatternValidator;
import dslab.protocol.general.instruction.validator.impl.ArgumentRequiredValidator;
import dslab.protocol.general.instruction.validator.impl.NoArgumentValidator;

import java.util.List;

/**
 * A concrete implementation of {@code InstructionMapBase}.
 *
 * @see InstructionMapBase
 */
public final class DMTPInstructionMap extends InstructionMapBase {


    private final Hook<Boolean, String> acceptHook;
    private final Hook<Boolean, String> isKnownHook;

    public DMTPInstructionMap(final Hook<Boolean, String> acceptHook,
                              final Hook<Boolean, String> isKnownHook) {
        super();
        this.acceptHook = acceptHook;
        this.isKnownHook = isKnownHook;
    }

    @Override
    public void loadInstructions() {
        DMTPBeginInstruction begin = new DMTPBeginInstruction();
        DMTPFromInstruction from = new DMTPFromInstruction();
        DMTPToInstruction to = new DMTPToInstruction();
        DMTPSubjectInstruction subject = new DMTPSubjectInstruction();
        DMTPDataInstruction data = new DMTPDataInstruction();
        DMTPSendInstruction send = new DMTPSendInstruction();
        DMTPQuitInstruction quit = new DMTPQuitInstruction();

        begin.setDependentInstructions(
            List.of(
                from,
                to,
                subject,
                data,
                send
            ));
        from.setRequiredInstructions(
            List.of(
                begin
            ));
        to.setRequiredInstructions(
            List.of(
                begin
            ));
        subject.setRequiredInstructions(
            List.of(
                begin
            ));
        data.setRequiredInstructions(
            List.of(
                begin
            ));
        send.setRequiredInstructions(
            List.of(
                begin,
                from,
                to,
                subject,
                data
            ));
        quit.setDependentInstructions(
            List.of(
                begin,
                from,
                to,
                subject,
                data,
                send
            ));

        begin.setValidators(
            List.of(
                new NoArgumentValidator("begin")
            ));
        from.setValidators(
            List.of(
                new ArgumentRequiredValidator("from"),
                new ArgumentPartMinValidator("from", ",", 1),
                new ArgumentPartMaxValidator("from", ",", 1),
                new ArgumentPartPatternValidator("from",
                    "[^\\\\\\s,]+@[^\\\\\\s,]+", ",")
            ));
        to.setValidators(
            List.of(
                new ArgumentRequiredValidator("to"),
                new ArgumentPartMinValidator("to", ",", 1),
                new ArgumentPartPatternValidator("to",
                    "[^\\\\\\s,]+@[^\\\\\\s,]+", ",")
            ));
        subject.setValidators(
            List.of(
                new ArgumentRequiredValidator("subject")
            ));
        data.setValidators(
            List.of(
                new ArgumentRequiredValidator("data")
            ));
        send.setValidators(
            List.of(
                new NoArgumentValidator("send")
            ));
        quit.setValidators(
            List.of(
                new NoArgumentValidator("quit")
            ));

        to.setAcceptHook(acceptHook);
        to.setIsKnownHook(isKnownHook);

        registerInstruction("begin", begin);
        registerInstruction("to", to);
        registerInstruction("from", from);
        registerInstruction("subject", subject);
        registerInstruction("data", data);
        registerInstruction("send", send);
        registerInstruction("quit", quit);
    }
}
