package kakapo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitItemDestination {

    private String _userGuid;
    private Long _preKeyId;
    private String _encryptedGroupKey;
    private String _nonce;

    @JsonProperty("userGuid")
    public String getUserGuid() {
        return _userGuid;
    }

    public void setUserGuid(String userGuid) {
        _userGuid = userGuid;
    }

    @JsonProperty("preKeyId")
    public Long getPreKeyId() {
        return _preKeyId;
    }

    public void setPreKeyId(Long preKeyId) {
        _preKeyId = preKeyId;
    }

    @JsonProperty("encryptedGroupKey")
    public String getEncryptedGroupKey() {
        return _encryptedGroupKey;
    }

    public void setEncryptedGroupKey(String encryptedGroupKey) {
        _encryptedGroupKey = encryptedGroupKey;
    }

    @JsonProperty("nonce")
    public String getNonce() {
        return _nonce;
    }

    public void setNonce(String nonce) {
        _nonce = nonce;
    }
}
