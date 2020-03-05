package kakapo.api.request;

import kakapo.util.SerializationUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

public class SubmitItemRequest extends SignedRequest {

    private Long _parentItemRemoteId;
    private Collection<String> _sharedWithGuids;

    @Override
    protected void serializeMessageDigest(DataOutputStream outputStream) throws IOException {
        SerializationUtil.writeOptionalLong(outputStream, _parentItemRemoteId);
        for (String sharedWithGuid : _sharedWithGuids) {
            outputStream.writeUTF(sharedWithGuid);
        }
    }

    @JsonProperty("pir")
    public Long getParentItemRemoteId() {
        return _parentItemRemoteId;
    }

    public void setParentItemRemoteId(Long parentItemRemoteId) {
        _parentItemRemoteId = parentItemRemoteId;
    }

    @JsonProperty("swg")
    public Collection<String> getSharedWithGuids() {
        return _sharedWithGuids;
    }

    public void setSharedWithGuids(Collection<String> sharedWithGuids) {
        _sharedWithGuids = sharedWithGuids;
    }
}
