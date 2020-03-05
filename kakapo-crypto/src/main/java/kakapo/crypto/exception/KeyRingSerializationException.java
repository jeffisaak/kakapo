package kakapo.crypto.exception;

public class KeyRingSerializationException extends CryptoException {

    public KeyRingSerializationException(String message, Exception cause)
    {
        super(message, cause);
    }

    public KeyRingSerializationException(Exception cause) {
        super(cause);
    }
}
