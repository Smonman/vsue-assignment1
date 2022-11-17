package dslab.protocol.dmtp.parser;

import dslab.protocol.dmtp.message.DMTPMessage;
import dslab.protocol.general.instruction.Instruction;
import dslab.protocol.general.instruction.map.InstructionMap;
import dslab.protocol.general.parser.impl.ParserBase;

import java.util.LinkedList;
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
     * Gets the message.
     *
     * @return a new message.
     */
    public DMTPMessage getMessage() {
        return new DMTPMessage(
                getInstructionMap().get("from").getValue(),
                getRecipientsAsList(),
                getInstructionMap().get("subject").getValue(),
                getInstructionMap().get("data").getValue()
        );
    }

    /**
     * Gets all messages.
     *
     * <p>This method returns all possible messages from the cross product of
     * the given recipients and the other parts of the message. Each message
     * will have a different primary recipient, which will be the first
     * element of the recipients list.
     *
     * @apiNote this must be complete before a call to this.
     * @see DMTPParser#isComplete()
     */
    public List<DMTPMessage> getMessages() {
        List<String> recipients = getRecipientsAsList();
        List<DMTPMessage> messages = new LinkedList<>();
        for (String recipient : recipients) {
            messages.add(new DMTPMessage(
                    getInstructionMap().get("from").getValue(),
                    createRecipiantsList(recipient),
                    getInstructionMap().get("subject").getValue(),
                    getInstructionMap().get("data").getValue()
            ));
        }
        return messages;
    }

    private List<String> getRecipientsAsList() {
        return List.of(getInstructionMap()
                .get("to")
                .getValue()
                .split(","));
    }

    private List<String> createRecipiantsList(final String primaryRecipient) {
        List<String> temp = new LinkedList<>();
        temp.add(primaryRecipient);
        temp.addAll(getRecipientsAsList()
                .stream()
                .filter(r -> !r.equals(primaryRecipient))
                .collect(Collectors.toList()));
        return temp;
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
