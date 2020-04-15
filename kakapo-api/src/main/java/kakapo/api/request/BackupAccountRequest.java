package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BackupAccountRequest {

    private Long _backupVersionToUpdate;
    private String _nonce;

    @JsonProperty("backupVersionToUpdate")
    public Long getBackupVersionToUpdate() {
        return _backupVersionToUpdate;
    }

    public void setBackupVersionToUpdate(Long backupVersionToUpdate) {
        _backupVersionToUpdate = backupVersionToUpdate;
    }

    @JsonProperty("nonce")
    public String getNonce() {
        return _nonce;
    }

    public void setNonce(String nonce) {
        _nonce = nonce;
    }
}
