package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BackupAccountResponse {

    public static BackupAccountResponse success(long backupVersion) {
        BackupAccountResponse result = new BackupAccountResponse();
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
