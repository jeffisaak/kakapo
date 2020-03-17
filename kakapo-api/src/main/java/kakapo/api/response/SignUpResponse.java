package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpResponse {

    public static SignUpResponse success(String guid) {
        SignUpResponse result = new SignUpResponse();
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
