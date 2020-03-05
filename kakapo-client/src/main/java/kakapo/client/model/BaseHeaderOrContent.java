package kakapo.client.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Abstract superclass for all header/content objects.
 */
public abstract class BaseHeaderOrContent {

    /**
     * Deserialize the data input stream and populate this.  Two very important notes: First, the
     * item type is not deserialized, and deserialization assumes that we start after it. Second,
     * the data input stream is not closed by this method.
     *
     * @param dataInputStream The input stream to deserialize from.
     * @throws IOException if an error occurs reading from the stream.
     */
    public final void deserialize(DataInputStream dataInputStream) throws IOException {
        deserializeInternal(dataInputStream);
    }

    /**
     * Perform subclass-specific deserialization.
     *
     * @param dataInputStream The input stream to deserialize from.
     * @throws IOException if an error occurs reading from the stream.
     */
    protected void deserializeInternal(DataInputStream dataInputStream) throws IOException {
        // Noop. Subclasses may override.
    }

    /**
     * Serialize this into the data output stream.
     *
     * @param dataOutputStream The output stream to serialize to.
     * @throws IOException if an error occurs writing to the stream.
     */
    public final void serialize(DataOutputStream dataOutputStream) throws IOException {
        serializeInternal(dataOutputStream);
    }

    /**
     * Perform subclass-specific serialization.
     *
     * @param dataOutputStream The output stream to serialize to.
     * @throws IOException if an error occurs writing to the stream.
     */
    protected void serializeInternal(DataOutputStream dataOutputStream) throws IOException {
        // Noop. Subclasses may override.
    }

}
