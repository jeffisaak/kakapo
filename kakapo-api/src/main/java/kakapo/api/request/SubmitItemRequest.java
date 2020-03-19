package kakapo.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import kakapo.api.model.SubmitItemDestination;

import java.util.List;

public class SubmitItemRequest {

    private String _headerNonce;
    private String _contentNonce;
    private List<SubmitItemDestination> _destinations;
    private String _keyExchangePublicKey;

    @JsonProperty("headerNonce")
    public String getHeaderNonce() {
        return _headerNonce;
    }

    public void setHeaderNonce(String headerNonce) {
        _headerNonce = headerNonce;
    }

    @JsonProperty("contentNonce")
    public String getContentNonce() {
        return _contentNonce;
    }

    public void setContentNonce(String contentNonce) {
        _contentNonce = contentNonce;
    }

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
