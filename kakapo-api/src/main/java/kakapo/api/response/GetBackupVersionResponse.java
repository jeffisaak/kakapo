package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetBackupVersionResponse {

    public static GetBackupVersionResponse success(Long backupVersion) {
        GetBackupVersionResponse result = new GetBackupVersionResponse();
        result.setBackupVersion(backupVersion);
        return result;
    }

    private Long _backupVersion;

    @JsonProperty("backupVersion")
    public Long getBackupVersion() {
        return _backupVersion;
    }

    public void setBackupVersion(Long backupVersion) {
        _backupVersion = backupVersion;
    }
}
