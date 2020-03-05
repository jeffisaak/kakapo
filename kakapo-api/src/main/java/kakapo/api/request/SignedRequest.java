package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import kakapo.api.exception.SerializationException;
import kakapo.util.HashUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class SignedRequest {

    private String _guid;
    private byte[] _signature;

    @JsonIgnore
    public final byte[] getMessageDigest() throws SerializationException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            // GUID plus subclass-specific data (if provided).
            dataOutputStream.writeUTF(_guid);
            serializeMessageDigest(dataOutputStream);
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new SerializationException(e);
        } finally {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                // Ignore.
            }
        }

        // Hash the result.
        return HashUtil.sha256ToByteArray(byteArrayOutputStream.toByteArray());
    }

    protected void serializeMessageDigest(DataOutputStream outputStream) throws IOException {
        // Noop. Subclasses may override.
    }

    @JsonProperty("guid")
    public final String getGuid() {
        return _guid;
    }

    public final void setGuid(String guid) {
        _guid = guid;
    }

    @JsonProperty("sig")
    public final byte[] getSignature() {
        return _signature;
    }

    public final void setSignature(byte[] signature) {
        _signature = signature;
    }
}
