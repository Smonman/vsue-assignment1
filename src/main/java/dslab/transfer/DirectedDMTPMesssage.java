package dslab.transfer;

import dslab.protocol.dmtp.message.DMTPMessage;

/**
 * This represents a DMTP message with a domain it is directed to.
 *
 * @see dslab.protocol.dmtp.message.DMTPMessage
 */
public class DirectedDMTPMesssage {

    private final DMTPMessage message;
    private final String domain;

    public DirectedDMTPMesssage(DMTPMessage message, String domain) {
        this.message = message;
        this.domain = domain;
    }

    public DMTPMessage getMessage() {
        return message;
    }

    public String getDomain() {
        return domain;
    }
}
