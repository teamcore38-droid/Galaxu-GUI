package util;

public class ValidationUtils {

    /**
     * Checks if a string is null or empty.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Parses a string to a positive double. Returns -1 if invalid.
     */
    public static double parsePositiveDouble(String str) {
        if (isEmpty(str)) {
            return -1;
        }
        try {
            double value = Double.parseDouble(str);
            return value >= 0 ? value : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Parses a string to a positive integer. Returns -1 if invalid.
     */
    public static int parsePositiveInt(String str) {
        if (isEmpty(str)) {
            return -1;
        }
        try {
            int value = Integer.parseInt(str);
            return value >= 0 ? value : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Validates that password is at least 6 characters long.
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Validates that a username is alphanumeric and has length between 3 and 20.
     */
    public static boolean isValidUsername(String username) {
        if (isEmpty(username)) return false;
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
}
