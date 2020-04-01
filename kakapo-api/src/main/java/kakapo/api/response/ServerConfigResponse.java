package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerConfigResponse {

    public static ServerConfigResponse success(long quotaPerUser,
                                               long headerSizeLimit,
                                               long contentSizeLimit,
                                               long accountBackupSizeLimit) {
        ServerConfigResponse result = new ServerConfigResponse();
        result.setQuotaPerUser(quotaPerUser);
        result.setHeaderSizeLimit(headerSizeLimit);
        result.setContentSizeLimit(contentSizeLimit);
        result.setAccountBackupSizeLimit(accountBackupSizeLimit);
        return result;
    }

    private long _quotaPerUser;
    private long _headerSizeLimit;
    private long _contentSizeLimit;
    private long _accountBackupSizeLimit;

    @JsonProperty("quotaPerUser")
    public long getQuotaPerUser() {
        return _quotaPerUser;
    }

    public void setQuotaPerUser(long quotaPerUser) {
        _quotaPerUser = quotaPerUser;
    }

    @JsonProperty("headerSizeLimit")
    public long getHeaderSizeLimit() {
        return _headerSizeLimit;
    }

    public void setHeaderSizeLimit(long headerSizeLimit) {
        _headerSizeLimit = headerSizeLimit;
    }

    @JsonProperty("contentSizeLimit")
    public long getContentSizeLimit() {
        return _contentSizeLimit;
    }

    public void setContentSizeLimit(long contentSizeLimit) {
        _contentSizeLimit = contentSizeLimit;
    }

    @JsonProperty("accountBackupSizeLimit")
    public long getAccountBackupSizeLimit() {
        return _accountBackupSizeLimit;
    }

    public void setAccountBackupSizeLimit(long accountBackupSizeLimit) {
        _accountBackupSizeLimit = accountBackupSizeLimit;
    }
}
