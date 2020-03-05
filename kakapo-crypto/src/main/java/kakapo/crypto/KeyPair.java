package kakapo.crypto;

/**
 * Container for a couple of byte arrays that represent a secret and public key.
 */
public class KeyPair {

    private byte[] _secretKey;
    private byte[] _publicKey;

    public KeyPair(byte[] secretKey, byte[] publicKey) {
        _secretKey = secretKey;
        _publicKey = publicKey;
    }

    public byte[] getSecretKey() {
        return _secretKey;
    }

    public byte[] getPublicKey() {
        return _publicKey;
    }
}
