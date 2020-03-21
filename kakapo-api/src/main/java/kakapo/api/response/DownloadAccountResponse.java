package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DownloadAccountResponse {

    public static DownloadAccountResponse success(byte[] encryptedAccountData) {
        DownloadAccountResponse result = new DownloadAccountResponse();
        result.setEncryptedAccountData(encryptedAccountData);
        return result;
    }

    private byte[] _encryptedAccountData;

    @JsonProperty("encryptedAccountData")
    public byte[] getEncryptedAccountData() {
        return _encryptedAccountData;
    }

    public void setEncryptedAccountData(byte[] encryptedAccountData) {
        _encryptedAccountData = encryptedAccountData;
    }
}
