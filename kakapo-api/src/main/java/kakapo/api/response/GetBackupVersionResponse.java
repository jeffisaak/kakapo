package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetBackupVersionResponse {

    public static GetBackupVersionResponse success(Long backupVersion, String backupHash) {
        GetBackupVersionResponse result = new GetBackupVersionResponse();
        result.setBackupVersion(backupVersion);
        result.setBackupHash(backupHash);
        return result;
    }

    private Long _backupVersion;
    private String _backupHash;

    @JsonProperty("backupVersion")
    public Long getBackupVersion() {
        return _backupVersion;
    }

    public void setBackupVersion(Long backupVersion) {
        _backupVersion = backupVersion;
    }

    @JsonProperty("backupHash")
    public String getBackupHash() {
        return _backupHash;
    }

    public void setBackupHash(String backupHash) {
        _backupHash = backupHash;
    }
}
