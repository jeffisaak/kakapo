package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadAccountResponse {

    public static UploadAccountResponse success(String guid) {
        UploadAccountResponse result = new UploadAccountResponse();
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
