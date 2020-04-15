package kakapo.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kakapo.api.model.ShareItem;

import java.util.List;

public class FetchItemHeadersResponse {

    public static FetchItemHeadersResponse success(List<ShareItem> shareItems, long remainingItemCount) {
        FetchItemHeadersResponse result = new FetchItemHeadersResponse();
        result.setShareItems(shareItems);
        result.setRemainingItemCount(remainingItemCount);
        return result;
    }

    private List<ShareItem> _shareItems;
    private Long _remainingItemCount;

    @JsonProperty("shareItems")
    public List<ShareItem> getShareItems() {
        return _shareItems;
    }

    public void setShareItems(List<ShareItem> shareItems) {
        _shareItems = shareItems;
    }

    @JsonProperty("remainingItemCount")
    public Long getRemainingItemCount() {
        return _remainingItemCount;
    }

    public void setRemainingItemCount(Long remainingItemCount) {
        _remainingItemCount = remainingItemCount;
    }
}
