package kakapo.crypto.exception;

public class KeyGenerationException extends CryptoException {

    public KeyGenerationException(String message, Exception cause)
    {
        super(message, cause);
    }

    public KeyGenerationException(Exception cause) {
        super(cause);
    }
}
