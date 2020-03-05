package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuotaResponse {

    public static QuotaResponse success(int usedQuota, int maxQuota) {
        QuotaResponse result = new QuotaResponse();
        result.setUsedQuota(usedQuota);
        result.setMaxQuota(maxQuota);
        return result;
    }

    private int _usedQuota;
    private int _maxQuota;

    @JsonProperty("uq")
    public int getUsedQuota() {
        return _usedQuota;
    }

    public void setUsedQuota(int usedQuota) {
        _usedQuota = usedQuota;
    }

    @JsonProperty("mq")
    public int getMaxQuota() {
        return _maxQuota;
    }

    public void setMaxQuota(int maxQuota) {
        _maxQuota = maxQuota;
    }
}
