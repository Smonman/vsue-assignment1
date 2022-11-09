package dslab.util;

/**
 * This is a static utility class.
 *
 * <p>It provides methods to parse a login argument in the form of:
 * <username> <password>
 */
public final class LoginParser {

    private LoginParser() {
    }

    /**
     * Retrieves the username of a username password pair.
     *
     * <p>If {@code usernamePasswordPair} is {@code null}, {@code null} is
     * returned. The username password pair has to be of the specified format.
     *
     * @param usernamePasswordPair username and password separated by a space
     * @return the username or {@code null}
     */
    public static String getUsername(final String usernamePasswordPair) {
        if (usernamePasswordPair == null) {
            return null;
        }
        return usernamePasswordPair.trim().split(" ")[0];
    }

    /**
     * Retrieves the password of a username password pair.
     *
     * <p>If {@code usernamePasswordPair} is {@code null}, {@code null} is
     * returned. The username password pair has to be of the specified format.
     *
     * @param usernamePasswordPair username and password separated by a space
     * @return the password or {@code null}
     */
    public static String getPassword(final String usernamePasswordPair) {
        if (usernamePasswordPair == null) {
            return null;
        }
        return usernamePasswordPair.trim().split(" ")[1];
    }
}
