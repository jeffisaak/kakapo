package kakapo.crypto.exception;

public class CheckKeyLengthException extends CryptoException {

    public CheckKeyLengthException(String message, Exception cause)
    {
        super(message, cause);
    }

    public CheckKeyLengthException(Exception cause) {
        super(cause);
    }
}
