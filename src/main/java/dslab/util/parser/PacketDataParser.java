package dslab.util.parser;

/**
 * This is a static utility class.
 *
 * <p>It provides methods to parse packet data in the form of:
 * <host>:<port> <email-address>
 */
public final class PacketDataParser {

    private PacketDataParser() {
    }

    /**
     * Retrieves the host from the packet data.
     *
     * <p>If {@code packetData} is {@code null}, {@code null} is
     * returned. The packetData has to be of the specified format.
     *
     * @param packetData the packet data
     * @return the host
     */
    public static String getHost(String packetData) {
        if (packetData == null) {
            return null;
        }
        return packetData.trim().split(":", 2)[0];
    }

    /**
     * Retrieves the port from the packet data.
     *
     * <p>If {@code packetData} is {@code null}, {@code null} is
     * returned. The packetData has to be of the specified format.
     *
     * @param packetData the packet data
     * @return the port
     */
    public static Integer getPort(String packetData) {
        if (packetData == null) {
            return null;
        }
        return Integer.parseInt(packetData.trim().split(":", 2)[1]
            .split(" ", 2)[0]);
    }

    /**
     * Retrieves the host and port from the packet data.
     *
     * <p>If {@code packetData} is {@code null}, {@code null} is
     * returned. The packetData has to be of the specified format.
     *
     * @param packetData the packet data
     * @return the host port pair
     */
    public static String getHostPortPair(String packetData) {
        if (packetData == null) {
            return null;
        }
        return String.format("%s:%d", getHost(packetData), getPort(packetData));
    }

    /**
     * Retrieves the address from the packet data.
     *
     * <p>If {@code packetData} is {@code null}, {@code null} is
     * returned. The packetData has to be of the specified format.
     *
     * @param packetData the packet data
     * @return the port
     */
    public static String getAddress(String packetData) {
        if (packetData == null) {
            return null;
        }
        return packetData.trim().split(" ", 2)[1];
    }
}
