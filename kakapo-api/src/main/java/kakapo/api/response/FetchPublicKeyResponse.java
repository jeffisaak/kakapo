package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchPublicKeyResponse {

    public static FetchPublicKeyResponse success(String signingPublicKey) {
        FetchPublicKeyResponse result = new FetchPublicKeyResponse();
        result.setSigningPublicKey(signingPublicKey);
        return result;
    }

    private String _signingPublicKey;

    @JsonProperty("signingPublicKey")
    public String getSigningPublicKey() {
        return _signingPublicKey;
    }

    public void setSigningPublicKey(String signingPublicKey) {
        _signingPublicKey = signingPublicKey;
    }
}
