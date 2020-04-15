package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UploadPreKeysRequest {

    private List<String> _signedPreKeys;

    @JsonProperty("signedPreKeys")
    public List<String> getSignedPreKeys() {
        return _signedPreKeys;
    }

    public void setSignedPreKeys(List<String> signedPreKeys) {
        _signedPreKeys = signedPreKeys;
    }
}
