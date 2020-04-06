package kakapo.crypto;

public class HashResult {
    private final byte[] _hash;
    private final byte[] _salt;

    public HashResult(byte[] hash, byte[] salt) {
        _hash = hash;
        _salt = salt;
    }

    public byte[] getHash() {
        return _hash;
    }

    public byte[] getSalt() {
        return _salt;
    }
}
