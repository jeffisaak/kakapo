package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.DataOutputStream;
import java.io.IOException;

public class StreamContentRequest extends SignedRequest {

    private Long _itemRemoteId;

    @Override
    protected void serializeMessageDigest(DataOutputStream outputStream) throws IOException {
        outputStream.writeLong(_itemRemoteId);
    }

    @JsonProperty("ir")
    public Long getItemRemoteId() {
        return _itemRemoteId;
    }

    public void setItemRemoteId(Long itemRemoteId) {
        _itemRemoteId = itemRemoteId;
    }
}
