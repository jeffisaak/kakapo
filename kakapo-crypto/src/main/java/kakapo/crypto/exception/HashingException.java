package kakapo.crypto.exception;

public class HashingException extends CryptoException {

    public HashingException(String message) {
        super(message);
    }

    public HashingException(String message, Exception cause)
    {
        super(message, cause);
    }

    public HashingException(Exception cause) {
        super(cause);
    }
}
