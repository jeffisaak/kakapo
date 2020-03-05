package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import kakapo.util.SerializationUtil;

import java.io.DataOutputStream;
import java.io.IOException;

public class BlacklistRequest extends SignedRequest {

    private String _targetGuid;

    @Override
    protected void serializeMessageDigest(DataOutputStream outputStream) throws IOException {
        SerializationUtil.writeOptionalUTF(outputStream, _targetGuid);
    }

    @JsonProperty("tg")
    public String getTargetGuid() {
        return _targetGuid;
    }

    public void setTargetGuid(String targetGuid) {
        _targetGuid = targetGuid;
    }
}
