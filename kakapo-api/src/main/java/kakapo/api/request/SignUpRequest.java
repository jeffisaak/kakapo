package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpRequest {

    private String _guid;
    private byte[] _publicKeys;

    @JsonProperty("guid")
    public String getGuid() {
        return _guid;
    }

    public void setGuid(String guid) {
        _guid = guid;
    }

    @JsonProperty("pk")
    public byte[] getPublicKeys() {
        return _publicKeys;
    }

    public void setPublicKeys(byte[] publicKeys) {
        _publicKeys = publicKeys;
    }
}
