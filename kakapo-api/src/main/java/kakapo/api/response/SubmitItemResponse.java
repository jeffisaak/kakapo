package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmitItemResponse {

    public static SubmitItemResponse success(long itemRemoteId, int usedQuota, int maxQuota) {
        SubmitItemResponse result = new SubmitItemResponse();
        result.setItemRemoteId(itemRemoteId);
        result.setUsedQuota(usedQuota);
        result.setMaxQuota(maxQuota);
        return result;
    }

    private long _itemRemoteId;
    private int _usedQuota;
    private int _maxQuota;

    @JsonProperty("itemRemoteId")
    public long getItemRemoteId() {
        return _itemRemoteId;
    }

    public void setItemRemoteId(long itemRemoteId) {
        _itemRemoteId = itemRemoteId;
    }

    @JsonProperty("usedQuota")
    public int getUsedQuota() {
        return _usedQuota;
    }

    public void setUsedQuota(int usedQuota) {
        _usedQuota = usedQuota;
    }

    @JsonProperty("maxQuota")
    public int getMaxQuota() {
        return _maxQuota;
    }

    public void setMaxQuota(int maxQuota) {
        _maxQuota = maxQuota;
    }
}
