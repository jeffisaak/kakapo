package kakapo.api.request;

import kakapo.util.SerializationUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.DataOutputStream;
import java.io.IOException;

public class UploadAccountRequest {

    private byte[] _nonce;
    private byte[] _encryptedAccountData;

    @Override
    protected void serializeMessageDigest(DataOutputStream outputStream) throws IOException {
        SerializationUtil.writeBytes(outputStream, _nonce);
        SerializationUtil.writeBytes(outputStream, _encryptedAccountData);
    }

    @JsonProperty("nonce")
    public byte[] getNonce() {
        return _nonce;
    }

    public void setNonce(byte[] nonce) {
        _nonce = nonce;
    }

    @JsonProperty("encryptedAccountData")
    public byte[] getEncryptedAccountData() {
        return _encryptedAccountData;
    }

    public void setEncryptedAccountData(byte[] encryptedAccountData) {
        _encryptedAccountData = encryptedAccountData;
    }
}
