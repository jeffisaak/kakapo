package kakapo.crypto;

public class HashAndEncryptResult {

    private HashResult _hashResult;
    private EncryptionResult _encryptionResult;

    public HashAndEncryptResult(HashResult hashResult, EncryptionResult encryptionResult) {
        _hashResult = hashResult;
        _encryptionResult = encryptionResult;
    }

    public HashResult getHashResult() {
        return _hashResult;
    }

    public EncryptionResult getEncryptionResult() {
        return _encryptionResult;
    }
}
