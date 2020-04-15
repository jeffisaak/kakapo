package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignUpResponse {

    public static SignUpResponse success(String guid, String apiKey) {
        SignUpResponse result = new SignUpResponse();
        result.setGuid(guid);
        result.setApiKey(apiKey);
        return result;
    }

    private String _guid;
    private String _apiKey;

    @JsonProperty("guid")
    public String getGuid() {
        return _guid;
    }

    public void setGuid(String guid) {
        _guid = guid;
    }

    @JsonProperty("apiKey")
    public String getApiKey() {
        return _apiKey;
    }

    public void setApiKey(String apiKey) {
        _apiKey = apiKey;
    }
}
