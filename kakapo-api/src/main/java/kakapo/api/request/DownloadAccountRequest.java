package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DownloadAccountRequest {

    private String _guid;

    @JsonProperty("guid")
    public String getGuid() {
        return _guid;
    }

    public void setGuid(String guid) {
        _guid = guid;
    }
}
