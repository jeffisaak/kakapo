package kakapo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShareItemRecipient {

    private String _guid;
    private String _signingPublicKey;

    @JsonProperty("guid")
    public String getGuid() {
        return _guid;
    }

    public void setGuid(String guid) {
        _guid = guid;
    }

    @JsonProperty("spk")
    public String getSigningPublicKey() {
        return _signingPublicKey;
    }

    public void setSigningPublicKey(String signingPublicKey) {
        _signingPublicKey = signingPublicKey;
    }
}
