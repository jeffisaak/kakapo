package kakapo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShareItem {

    private long _remoteId;
    private String _ownerGuid;
    private Long _parentItemRemoteId;
    private boolean _markedAsDeleted;
    private boolean _blacklisted;
    private long _itemTimestamp;
    private String _encryptedHeader;
    private String _headerNonce;
    private long _childCount;
    private long _preKeyId;
    private String _keyExchangePublicKey;
    private String _encryptedGroupKey;
    private String _nonce;

    @JsonProperty("remoteId")
    public long getRemoteId() {
        return _remoteId;
    }

    public void setRemoteId(long remoteId) {
        _remoteId = remoteId;
    }

    @JsonProperty("ownerGuid")
    public String getOwnerGuid() {
        return _ownerGuid;
    }

    public void setOwnerGuid(String ownerGuid) {
        _ownerGuid = ownerGuid;
    }

    @JsonProperty("parentRemoteId")
    public Long getParentItemRemoteId() {
        return _parentItemRemoteId;
    }

    public void setParentItemRemoteId(Long parentItemRemoteId) {
        _parentItemRemoteId = parentItemRemoteId;
    }

    @JsonProperty("markedAsDeleted")
    public boolean isMarkedAsDeleted() {
        return _markedAsDeleted;
    }

    public void setMarkedAsDeleted(boolean markedAsDeleted) {
        _markedAsDeleted = markedAsDeleted;
    }

    @JsonProperty("blacklisted")
    public boolean isBlacklisted() {
        return _blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        _blacklisted = blacklisted;
    }

    @JsonProperty("itemTimestamp")
    public long getItemTimestamp() {
        return _itemTimestamp;
    }

    public void setItemTimestamp(long itemTimestamp) {
        _itemTimestamp = itemTimestamp;
    }

    @JsonProperty("encryptedHeader")
    public String getEncryptedHeader() {
        return _encryptedHeader;
    }

    public void setEncryptedHeader(String encryptedHeader) {
        _encryptedHeader = encryptedHeader;
    }

    @JsonProperty("headerNonce")
    public String getHeaderNonce() {
        return _headerNonce;
    }

    public void setHeaderNonce(String headerNonce) {
        _headerNonce = headerNonce;
    }

    @JsonProperty("childCount")
    public long getChildCount() {
        return _childCount;
    }

    public void setChildCount(long childCount) {
        _childCount = childCount;
    }

    @JsonProperty("preKeyId")
    public long getPreKeyId() {
        return _preKeyId;
    }

    public void setPreKeyId(long preKeyId) {
        _preKeyId = preKeyId;
    }

    @JsonProperty("keyExchangePublicKey")
    public String getKeyExchangePublicKey() {
        return _keyExchangePublicKey;
    }

    public void setKeyExchangePublicKey(String keyExchangePublicKey) {
        _keyExchangePublicKey = keyExchangePublicKey;
    }

    @JsonProperty("encryptedGroupKey")
    public String getEncryptedGroupKey() {
        return _encryptedGroupKey;
    }

    public void setEncryptedGroupKey(String encryptedGroupKey) {
        _encryptedGroupKey = encryptedGroupKey;
    }

    @JsonProperty("nonce")
    public String getNonce() {
        return _nonce;
    }

    public void setNonce(String nonce) {
        _nonce = nonce;
    }
}
