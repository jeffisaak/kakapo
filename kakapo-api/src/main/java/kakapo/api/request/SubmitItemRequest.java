package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import kakapo.api.model.SubmitItemDestination;

import java.util.List;

public class SubmitItemRequest {

    private List<SubmitItemDestination> _destinations;
    private String _keyExchangePublicKey;

    @JsonProperty("destinations")
    public List<SubmitItemDestination> getDestinations() {
        return _destinations;
    }

    public void setDestinations(List<SubmitItemDestination> destinations) {
        _destinations = destinations;
    }

    @JsonProperty("keyExchangePublicKey")
    public String getKeyExchangePublicKey() {
        return _keyExchangePublicKey;
    }

    public void setKeyExchangePublicKey(String keyExchangePublicKey) {
        _keyExchangePublicKey = keyExchangePublicKey;
    }
}
