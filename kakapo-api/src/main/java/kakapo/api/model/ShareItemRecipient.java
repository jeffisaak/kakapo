package kakapo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShareItemRecipient {

    private String _guid;
    private String _encryptionPublicKey;

    @JsonProperty("guid")
    public String getGuid() {
        return _guid;
    }

    public void setGuid(String guid) {
        _guid = guid;
    }

    @JsonProperty("epk")
    public String getEncryptionPublicKey() {
        return _encryptionPublicKey;
    }

    public void setEncryptionPublicKey(String encryptionPublicKey) {
        _encryptionPublicKey = encryptionPublicKey;
    }
}
