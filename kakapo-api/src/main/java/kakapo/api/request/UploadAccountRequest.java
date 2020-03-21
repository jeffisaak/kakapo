package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadAccountRequest {

    private byte[] _nonce;
    private byte[] _encryptedAccountData;

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
