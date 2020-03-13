package kakapo.crypto.exception;

public class DecryptFailedException extends CryptoException {

    public DecryptFailedException(String message) {
        super(message);
    }

    public DecryptFailedException(String message, Exception cause)
    {
        super(message, cause);
    }

    public DecryptFailedException(Exception cause) {
        super(cause);
    }
}
