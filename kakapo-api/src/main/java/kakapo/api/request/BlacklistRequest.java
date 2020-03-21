package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlacklistRequest {

    private String _targetGuid;

    @JsonProperty("targetGuid")
    public String getTargetGuid() {
        return _targetGuid;
    }

    public void setTargetGuid(String targetGuid) {
        _targetGuid = targetGuid;
    }
}
