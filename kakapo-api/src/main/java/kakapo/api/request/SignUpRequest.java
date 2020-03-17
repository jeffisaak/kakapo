package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpRequest {

    private String _signingPublicKey;

    @JsonProperty("spk")
    public String getSigningPublicKey() {
        return _signingPublicKey;
    }

    public void setSigningPublicKey(String signingPublicKey) {
        _signingPublicKey = signingPublicKey;
    }
}
