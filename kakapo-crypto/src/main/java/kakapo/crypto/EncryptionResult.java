package kakapo.crypto;

public class EncryptionResult {

    private final byte[] _ciphertext;
    private final byte[] _nonce;

    public EncryptionResult(byte[] ciphertext, byte[] nonce) {
        _ciphertext = ciphertext;
        _nonce = nonce;
    }

    public byte[] getCiphertext() {
        return _ciphertext;
    }

    public byte[] getNonce() {
        return _nonce;
    }
}
