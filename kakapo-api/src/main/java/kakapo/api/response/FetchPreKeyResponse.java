package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchPreKeyResponse {

    public static FetchPreKeyResponse success(Long preKeyId, String signedPreKey) {
        FetchPreKeyResponse result = new FetchPreKeyResponse();
        result.setPreKeyId(preKeyId);
        result.setSignedPreKey(signedPreKey);
        return result;
    }

    private Long _preKeyId;
    private String _signedPreKey;

    @JsonProperty("preKeyId")
    public Long getPreKeyId() {
        return _preKeyId;
    }

    public void setPreKeyId(Long preKeyId) {
        _preKeyId = preKeyId;
    }

    @JsonProperty("signedPreKey")
    public String getSignedPreKey() {
        return _signedPreKey;
    }

    public void setSignedPreKey(String signedPreKey) {
        _signedPreKey = signedPreKey;
    }
}
