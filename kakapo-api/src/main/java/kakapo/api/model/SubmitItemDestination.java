package kakapo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitItemDestination {

    private String _userGuid;
    private Long _preKeyId;
    private String _encryptedGroupKey;
    private String _groupKeyNonce;

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

    @JsonProperty("groupKeyNonce")
    public String getGroupKeyNonce() {
        return _groupKeyNonce;
    }

    public void setGroupKeyNonce(String groupKeyNonce) {
        _groupKeyNonce = groupKeyNonce;
    }
}
