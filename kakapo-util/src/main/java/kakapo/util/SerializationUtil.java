package kakapo.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Helper methods for serialization and deserialization of data.
 */
public class SerializationUtil {

    private SerializationUtil() {
        // Prevents instantiation.
    }

    /**
     * Reads bytes from the input stream into a byte array. Expects first an integer indicating the
     * length of the byte array, then the byte array of the appropriate length.
     *
     * @param inputStream The input stream to read from.
     * @return The read byte array, or null if it was not present.
     * @throws IOException If any of the reads fail or if the actual length read is different than
     *                     the integer indicating the byte array length.
     */
    public static byte[] readBytes(DataInputStream inputStream) throws IOException {
        int length = inputStream.readInt();
        if (length > 0) {
            byte[] result = new byte[length];
            int bytesRead = inputStream.read(result);
            if (bytesRead != length) {
                throw new IOException("Number of bytes available in input stream does not match indicating length of data");
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * Reads an optional UTF-8 encoded string from the input stream. Expects first a boolean
     * indicating whether or not a string is present, then the string if the boolean value is true.
     *
     * @param inputStream The input stream to read from.
     * @return The read string, or null if it was not present.
     * @throws IOException If any of the reads from the input stream fail.
     */
    public static String readOptionalUTF(DataInputStream inputStream) throws IOException {
        boolean valuePresent = inputStream.readBoolean();
        if (valuePresent) {
            return inputStream.readUTF();
        }
        return null;
    }

    /**
     * Writes an optional integer to the output stream. First writes a boolean indicating whether or
     * not the value is not null, then the integer value if it is present.
     *
     * @param outputStream The output stream to write data to.
     * @param value The value to write to the stream.
     * @throws IOException If any of the writes to the input stream fail.
     */
    public static void writeOptionalInteger(DataOutputStream outputStream, Integer value)
            throws IOException {
        outputStream.writeBoolean(value != null);
        if (value != null) {
            outputStream.writeInt(value);
        }
    }

    /**
     * Writes an optional long to the output stream. First writes a boolean indicating whether or
     * not the value is not null, then the long value if it is present.
     *
     * @param outputStream The output stream to write data to.
     * @param value The value to write to the stream.
     * @throws IOException If any of the writes to the input stream fail.
     */
    public static void writeOptionalLong(DataOutputStream outputStream, Long value)
            throws IOException {
        outputStream.writeBoolean(value != null);
        if (value != null) {
            outputStream.writeLong(value);
        }
    }

    /**
     * Writes a byte array to the output stream. First writes an integer indicating the length of
     * the byte array (0 if null), then the actual byte array if it is not null.
     *
     * @param outputStream The output stream to write data to.
     * @param value The value to write to the stream.
     * @throws IOException If any of the writes to the input stream fail.
     */
    public static void writeBytes(DataOutputStream outputStream, byte[] value) throws IOException {
        int length = value != null ? value.length : 0;
        outputStream.writeInt(length);
        if (length > 0) {
            outputStream.write(value);
        }
    }

    /**
     * Write an optional UTF-8 encoded string to the output stream. First writes a boolean
     * indicating whether or not the value is not null, then the string value if present.
     *
     * @param outputStream The output stream to write data to.
     * @param value The value to write to the stream.
     * @throws IOException If any of the writes to the input stream fail.
     */
    public static void writeOptionalUTF(DataOutputStream outputStream, String value) throws IOException {
        outputStream.writeBoolean(value != null);
        if (value != null) {
            outputStream.writeUTF(value);
        }
    }
}
