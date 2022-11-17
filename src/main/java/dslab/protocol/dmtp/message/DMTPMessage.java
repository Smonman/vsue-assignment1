package dslab.protocol.dmtp.message;

import dslab.util.parser.AddressParser;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final List<String> to;
    private final String subject;
    private final String data;

    public DMTPMessage(final String from,
                       final List<String> to,
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

    public List<String> getTo() {
        return to;
    }

    public String getPrimaryTo() {
        return to.get(0);
    }

    public Set<String> getToDomains() {
        return to
                .stream()
                .map(AddressParser::getDomain)
                .collect(Collectors.toSet());
    }

    public String getSubject() {
        return subject;
    }

    public String getData() {
        return data;
    }

    public List<String> getMessageInstructions() {
        List<String> temp = new LinkedList<>();
        temp.add(String.format("from %s", from));
        temp.add(String.format("to %s", String.join(",", to)));
        temp.add(String.format("subject %s", subject));
        temp.add(String.format("data %s", data));
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
                        String.format("%7s: %s", "to", String.join(", ", to)),
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
