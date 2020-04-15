package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kakapo.api.model.ShareItemRecipient;

import java.util.List;

public class FetchRecipientsResponse {

    public static FetchRecipientsResponse success(List<ShareItemRecipient> recipients) {
        FetchRecipientsResponse result = new FetchRecipientsResponse();
        result.setRecipients(recipients);
        return result;
    }

    private List<ShareItemRecipient> _recipients;

    @JsonProperty("recipients")
    public List<ShareItemRecipient> getRecipients() {
        return _recipients;
    }

    public void setRecipients(List<ShareItemRecipient> recipients) {
        _recipients = recipients;
    }
}
