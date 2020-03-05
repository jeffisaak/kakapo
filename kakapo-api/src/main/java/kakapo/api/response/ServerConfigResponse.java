package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerConfigResponse {

    public static ServerConfigResponse success(long maxPublicKeyLength, long quotaPerUser,
                                               long headerSizeLimit, long contentSizeLimit,
                                               long uploadedAccountSizeLimit) {
        ServerConfigResponse result = new ServerConfigResponse();
        result.setMaxPublicKeyLength(maxPublicKeyLength);
        result.setQuotaPerUser(quotaPerUser);
        result.setHeaderSizeLimit(headerSizeLimit);
        result.setContentSizeLimit(contentSizeLimit);
        result.setUploadedAccountSizeLimit(uploadedAccountSizeLimit);
        return result;
    }

    private long _maxPublicKeyLength;
    private long _quotaPerUser;
    private long _headerSizeLimit;
    private long _contentSizeLimit;
    private long _uploadedAccountSizeLimit;

    @JsonProperty("mpkl")
    public long getMaxPublicKeyLength() {
        return _maxPublicKeyLength;
    }

    public void setMaxPublicKeyLength(long maxPublicKeyLength) {
        _maxPublicKeyLength = maxPublicKeyLength;
    }

    @JsonProperty("qpu")
    public long getQuotaPerUser() {
        return _quotaPerUser;
    }

    public void setQuotaPerUser(long quotaPerUser) {
        _quotaPerUser = quotaPerUser;
    }

    @JsonProperty("hsl")
    public long getHeaderSizeLimit() {
        return _headerSizeLimit;
    }

    public void setHeaderSizeLimit(long headerSizeLimit) {
        _headerSizeLimit = headerSizeLimit;
    }

    @JsonProperty("csl")
    public long getContentSizeLimit() {
        return _contentSizeLimit;
    }

    public void setContentSizeLimit(long contentSizeLimit) {
        _contentSizeLimit = contentSizeLimit;
    }

    @JsonProperty("uasl")
    public long getUploadedAccountSizeLimit() {
        return _uploadedAccountSizeLimit;
    }

    public void setUploadedAccountSizeLimit(long uploadedAccountSizeLimit) {
        _uploadedAccountSizeLimit = uploadedAccountSizeLimit;
    }
}
