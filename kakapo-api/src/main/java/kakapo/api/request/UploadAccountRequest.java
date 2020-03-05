package kakapo.api.request;

import kakapo.util.SerializationUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.DataOutputStream;
import java.io.IOException;

public class UploadAccountRequest extends SignedRequest {

    private byte[] _encryptedAccountData;

    @Override
    protected void serializeMessageDigest(DataOutputStream outputStream) throws IOException {
        SerializationUtil.writeBytes(outputStream, _encryptedAccountData);
    }

    @JsonProperty("ead")
    public byte[] getEncryptedAccountData() {
        return _encryptedAccountData;
    }

    public void setEncryptedAccountData(byte[] encryptedAccountData) {
        _encryptedAccountData = encryptedAccountData;
    }
}
