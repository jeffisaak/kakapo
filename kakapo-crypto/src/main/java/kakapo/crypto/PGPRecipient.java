package kakapo.crypto;

/**
 * Container for a GUID and public key.
 */
public class PGPRecipient {
    
    private String _guid;
    private byte[] _publicKey;

    public PGPRecipient(String guid, byte[] publicKey) {
        _guid = guid;
        _publicKey = publicKey;
    }

    public String getGuid() {
        return _guid;
    }

    public byte[] getPublicKey() {
        return _publicKey;
    }
}
