package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class UploadPreKeysResponse {

    public static UploadPreKeysResponse success(Map<Long, String> idToKeyMap) {
        UploadPreKeysResponse result = new UploadPreKeysResponse();
        result.setIdToKeyMap(idToKeyMap);
        return result;
    }

    private Map<Long, String> _idToKeyMap;

    @JsonProperty("idToKeyMap")
    public Map<Long, String> getIdToKeyMap() {
        return _idToKeyMap;
    }

    public void setIdToKeyMap(Map<Long, String> idToKeyMap) {
        _idToKeyMap = idToKeyMap;
    }
}
