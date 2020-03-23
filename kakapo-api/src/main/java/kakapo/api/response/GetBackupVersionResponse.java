package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetBackupVersionResponse {

    public static GetBackupVersionResponse success(long backupVersion) {
        GetBackupVersionResponse result = new GetBackupVersionResponse();
        result.setBackupVersion(backupVersion);
        return result;
    }

    private long _backupVersion;

    @JsonProperty("backupVersion")
    public long getBackupVersion() {
        return _backupVersion;
    }

    public void setBackupVersion(long backupVersion) {
        _backupVersion = backupVersion;
    }
}
