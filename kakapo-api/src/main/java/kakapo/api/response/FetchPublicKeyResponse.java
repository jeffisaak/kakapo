package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchPublicKeyResponse {

    public static FetchPublicKeyResponse success(byte[] publicKeyRings) {
        FetchPublicKeyResponse result = new FetchPublicKeyResponse();
        result.setPublicKeyRings(publicKeyRings);
        return result;
    }

    private byte[] _publicKeyRings;

    @JsonProperty("pkr")
    public byte[] getPublicKeyRings() {
        return _publicKeyRings;
    }

    public void setPublicKeyRings(byte[] publicKeyRings) {
        _publicKeyRings = publicKeyRings;
    }
}
