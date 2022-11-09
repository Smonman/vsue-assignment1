package dslab.protocol.dmtp.message;

import java.util.LinkedList;
import java.util.List;

/**
 * This represents a message for the DMTP protocol.
 *
 * <p>A message consists of the fields:
 * <ul>
 * <li>from
 * <li>to
 * <li>subject
 * <li>data
 * </ul
 */
public final class DMTPMessage {

    private final String from;
    private final String to;
    private final String subject;
    private final String data;

    public DMTPMessage(final String from,
                       final String to,
                       final String subject,
                       final String data) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.data = data;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getData() {
        return data;
    }

    public List<String> getMessageInstructions() {
        List<String> temp = new LinkedList<>();
        temp.add(String.format("from %s", getFrom()));
        temp.add(String.format("to %s", getTo()));
        temp.add(String.format("subject %s", getSubject()));
        temp.add(String.format("data %s", getData()));
        return temp;
    }

    @Override
    public String toString() {
        return "from: '" + from + "'"
            + ", to: '" + to + "'"
            + ", subject: '" + subject + "'"
            + ", data: '" + data + "'";
    }

    /**
     * Gets the message in the correct form for the DMAP display.
     *
     * @return the DMAP display string
     */
    public String toDMAPString() {
        // \r\n is needed for the putty terminal
        return String.join(" \r\n",
            List.of(
                String.format("%7s: %s", "from", from),
                String.format("%7s: %s", "to", to),
                String.format("%7s: %s", "subject", subject),
                String.format("%7s: %s", "data", data)
            ));
    }

    /**
     * Gets the message in the correct form for the DMAP preview.
     *
     * @return the DMAP preview string
     */
    public String toPreviewString() {
        return String.join(" ", List.of(from, subject));
    }
}
