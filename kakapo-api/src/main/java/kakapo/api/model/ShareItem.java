package kakapo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShareItem {

    private long _remoteId;
    private String _ownerGuid;
    private Long _parentItemRemoteId;
    private boolean _markedAsDeleted;
    private boolean _blacklisted;
    private long _itemTimestamp;
    private byte[] _encryptedHeader;
    private byte[] _encryptedContent;
    private long _childCount;

    @JsonProperty("r")
    public long getRemoteId() {
        return _remoteId;
    }

    public void setRemoteId(long remoteId) {
        _remoteId = remoteId;
    }

    @JsonProperty("og")
    public String getOwnerGuid() {
        return _ownerGuid;
    }

    public void setOwnerGuid(String ownerGuid) {
        _ownerGuid = ownerGuid;
    }

    @JsonProperty("pir")
    public Long getParentItemRemoteId() {
        return _parentItemRemoteId;
    }

    public void setParentItemRemoteId(Long parentItemRemoteId) {
        _parentItemRemoteId = parentItemRemoteId;
    }

    @JsonProperty("mad")
    public boolean isMarkedAsDeleted() {
        return _markedAsDeleted;
    }

    public void setMarkedAsDeleted(boolean markedAsDeleted) {
        _markedAsDeleted = markedAsDeleted;
    }

    @JsonProperty("bl")
    public boolean isBlacklisted() {
        return _blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        _blacklisted = blacklisted;
    }

    @JsonProperty("it")
    public long getItemTimestamp() {
        return _itemTimestamp;
    }

    public void setItemTimestamp(long itemTimestamp) {
        _itemTimestamp = itemTimestamp;
    }

    @JsonProperty("eh")
    public byte[] getEncryptedHeader() {
        return _encryptedHeader;
    }

    public void setEncryptedHeader(byte[] encryptedHeader) {
        _encryptedHeader = encryptedHeader;
    }

    @JsonProperty("ec")
    public byte[] getEncryptedContent() {
        return _encryptedContent;
    }

    public void setEncryptedContent(byte[] encryptedContent) {
        _encryptedContent = encryptedContent;
    }

    @JsonProperty("cc")
    public long getChildCount() {
        return _childCount;
    }

    public void setChildCount(long childCount) {
        _childCount = childCount;
    }
}
