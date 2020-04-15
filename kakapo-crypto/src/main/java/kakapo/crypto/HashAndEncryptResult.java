package kakapo.crypto;

public class HashAndEncryptResult {

    private final HashResult _hashResult;
    private final EncryptionResult _encryptionResult;

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
