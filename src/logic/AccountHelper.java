package logic;

/**
 * AccountHelper
 * Singleton class for passing the username dependency
 */
public class AccountHelper {
    private static String username; // username used to connect to DB

    // getter
    public static String getUsername() {
        return username;
    }

    // setter
    public static void setUsername(String username) {
        AccountHelper.username = username;
    }

}
