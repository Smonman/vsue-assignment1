package dslab.util;

/**
 * This is a static utility class.
 *
 * <p>It provides methods to parse an address in the form of:
 * <username>@<domain>
 */
public final class AddressParser {

    private AddressParser() {
    }

    /**
     * Retrieves the username from an email address.
     *
     * <p>If {@code address} is {@code null}, {@code null} is
     * returned. The address has to be of the specified format.
     *
     * @param address the email address
     * @return the username of that email address
     */
    public static String getUsername(final String address) {
        if (address == null) {
            return null;
        }
        return address.trim().split("@")[0];
    }

    /**
     * Retrieves the domain from an email address.
     *
     * <p>If {@code address} is {@code null}, {@code null} is
     * returned. The address has to be of the specified format.
     *
     * @param address the email address
     * @return the domain of that email address
     */
    public static String getDomain(final String address) {
        if (address == null) {
            return null;
        }
        return address.trim().split("@")[1];
    }
}
