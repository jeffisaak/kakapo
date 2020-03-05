package kakapo.api.request;

import kakapo.util.SerializationUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.DataOutputStream;
import java.io.IOException;

public class FetchItemHeadersRequest extends SignedRequest {

    private Integer _itemCount;
    private Long _lastKnownItemRemoteId;
    private Long _parentItemRemoteId;
    private Long _itemRemoteId;

    @Override
    protected void serializeMessageDigest(DataOutputStream outputStream) throws IOException {
        SerializationUtil.writeOptionalInteger(outputStream, _itemCount);
        SerializationUtil.writeOptionalLong(outputStream, _lastKnownItemRemoteId);
        SerializationUtil.writeOptionalLong(outputStream, _parentItemRemoteId);
        SerializationUtil.writeOptionalLong(outputStream, _itemRemoteId);
    }

    @JsonProperty("ic")
    public Integer getItemCount() {
        return _itemCount;
    }

    public void setItemCount(Integer itemCount) {
        _itemCount = itemCount;
    }

    @JsonProperty("lkir")
    public Long getLastKnownItemRemoteId() {
        return _lastKnownItemRemoteId;
    }

    public void setLastKnownItemRemoteId(Long lastKnownItemRemoteId) {
        _lastKnownItemRemoteId = lastKnownItemRemoteId;
    }

    @JsonProperty("pir")
    public Long getParentItemRemoteId() {
        return _parentItemRemoteId;
    }

    public void setParentItemRemoteId(Long parentItemRemoteId) {
        _parentItemRemoteId = parentItemRemoteId;
    }

    @JsonProperty("ir")
    public Long getItemRemoteId() {
        return _itemRemoteId;
    }

    public void setItemRemoteId(Long itemRemoteId) {
        _itemRemoteId = itemRemoteId;
    }
}
