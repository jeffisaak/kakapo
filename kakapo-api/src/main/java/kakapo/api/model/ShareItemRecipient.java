package kakapo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShareItemRecipient {

    private String _guid;
    private byte[] _publicKeyData;

    @JsonProperty("guid")
    public String getGuid() {
        return _guid;
    }

    public void setGuid(String guid) {
        _guid = guid;
    }

    @JsonProperty("pkr")
    public byte[] getPublicKeyData() {
        return _publicKeyData;
    }

    public void setPublicKeyData(byte[] publicKeyData) {
        _publicKeyData = publicKeyData;
    }
}
