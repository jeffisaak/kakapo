package kakapo.crypto.exception;

public class SignMessageException extends CryptoException {

    public SignMessageException(String message, Exception cause)
    {
        super(message, cause);
    }

    public SignMessageException(Exception cause) {
        super(cause);
    }
}
