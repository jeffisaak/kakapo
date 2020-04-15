package kakapo.crypto.exception;

public class EncryptFailedException extends CryptoException {

    public EncryptFailedException(String message) {
        super(message);
    }

    public EncryptFailedException(String message, Exception cause)
    {
        super(message, cause);
    }

    public EncryptFailedException(Exception cause) {
        super(cause);
    }
}
