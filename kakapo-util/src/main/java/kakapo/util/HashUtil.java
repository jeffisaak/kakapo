package kakapo.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides hashing functions.
 */
public class HashUtil {

    private static final String SHA256 = "SHA-256";
    private static final String MD5 = "MD5";

    /**
     * Hash the input string with SHA-256 and return the result. If SHA-256 is unsupported or if the
     * encoding of the string into a byte array fails, returns null.
     *
     * @param input The string to be hashed.
     * @return The hashed string.
     */
    public static String sha256ToString(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        return sha256ToString(bytes);
    }

    /**
     * Hash the input byte array with SHA-256 and return the result as a string.
     *
     * @param input The byte array to be hashed.
     * @return The hashed string, or null if an exception occurred.
     */
    public static String sha256ToString(byte[] input) {
        return hashToString(input, SHA256);
    }

    /**
     * Hash the input byte array with SHA-256 and return the result as a byte array.
     *
     * @param input The byte array to be hashed.
     * @return The hashed byte array, or null if an exception occurred.
     */
    public static byte[] sha256ToByteArray(byte[] input) {
        return hashToByteArray(input, SHA256);
    }

    /**
     * Hash the input string with MD5 and return the result.
     *
     * @param input The string to be hashed.
     * @return The hashed string, or null if an exception occurred.
     */
    public static String md5(String input) {
        try {
            byte[] bytes = input.getBytes("UTF-8");
            return md5(bytes);
        } catch (UnsupportedEncodingException e) {
            // Swallow the exception.
            return null;
        }
    }

    /**
     * Hash the input byte array with MD5 and return the result.
     *
     * @param input The byte array to be hashed.
     * @return The hashed string or null if an exception occurred.
     */
    public static String md5(byte[] input) {
        return hashToString(input, MD5);
    }

    /**
     * Hash the input byte array with the specified algorithm and return the result as a string.
     *
     * @param input     The byte array to be hashed.
     * @param algorithm The hash algorithm to use.
     * @return The hashed string, or null if the algorithm could not be found.
     */
    private static String hashToString(byte[] input, String algorithm) {
        byte[] byteArray = hashToByteArray(input, algorithm);
        if (byteArray != null) {
            return String.format("%0" + (byteArray.length * 2) + 'x', new BigInteger(1, byteArray));
        }
        return null;
    }

    /**
     * Hash the input byte array with the specified algorithm and return the result as a byte
     * array.
     *
     * @param input     The byte array to be hashed.
     * @param algorithm The hash algorithm to be used.
     * @return The hashed byte array, or null if the algorithm could not be found.
     */
    private static byte[] hashToByteArray(byte[] input, String algorithm) {
        try {
            MessageDigest digest = null;
            digest = MessageDigest.getInstance(algorithm);
            digest.reset();
            return digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            // Swallow the exception.
            return null;
        }
    }
}
