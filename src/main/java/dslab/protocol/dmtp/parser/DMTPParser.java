package dslab.protocol.dmtp.parser;

import dslab.protocol.dmtp.message.DMTPMessage;
import dslab.protocol.general.instruction.Instruction;
import dslab.protocol.general.instruction.map.InstructionMap;
import dslab.protocol.general.parser.impl.ParserBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An extension of {@code ParserBase}.
 *
 * <p>This is a parser for the DMTP protocol.
 *
 * @see ParserBase
 */
public final class DMTPParser extends ParserBase {

    public DMTPParser(final InstructionMap instructionMap) {
        super(instructionMap);
    }

    /**
     * Gets all messages.
     *
     * <p>This method returns all possible messages from the cross product of
     * the given recipients and the other parts of the message. Each message
     * will therefore only have one recipient.
     *
     * @apiNote this must be complete before a call to this.
     * @see DMTPParser#isComplete()
     */
    public List<DMTPMessage> getMessages() {
        return Arrays.stream(getInstructionMap()
                .get("to")
                .getValue()
                .split(","))
            .map(recipient -> new DMTPMessage(
                getInstructionMap().get("from").getValue(),
                recipient,
                getInstructionMap().get("subject").getValue(),
                getInstructionMap().get("data").getValue()
            ))
            .collect(Collectors.toList());
    }

    /**
     * Checks whether this is complete.
     *
     * <p>This is complete when every part of the message is satisfied.
     *
     * @return true if the message is complete.
     */
    public boolean isComplete() {
        return getInstructionMap()
            .values()
            .stream()
            .filter(v -> !v.equals(getInstructionMap().get("quit")))
            .allMatch(Instruction::isSet);
    }
}
