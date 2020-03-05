package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestGuidResponse {

    public static RequestGuidResponse success(String guid) {
        RequestGuidResponse result = new RequestGuidResponse();
        result.setGuid(guid);
        return result;
    }

    private String _guid;

    @JsonProperty("guid")
    public String getGuid() {
        return _guid;
    }

    public void setGuid(String guid) {
        _guid = guid;
    }
}
