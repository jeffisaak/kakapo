package kakapo.util;

public class StringUtil {

    /**
     * Trim a string. If the result is empty, return null.
     *
     * @param input The string to trim.
     * @return The trimmed string, or null if the trimmed string is null or zero-length.
     */
    public static String trimToNull(String input) {
        if (input == null) {
            return null;
        }

        String result = input.trim();
        if (result.length() == 0) {
            return null;
        } else {
            return result;
        }
    }
}
