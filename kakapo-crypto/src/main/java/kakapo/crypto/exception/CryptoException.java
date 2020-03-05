package kakapo.crypto.exception;

/**
 * Abstract base class for all exceptions in this package.
 */
public abstract class CryptoException extends Exception {

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Exception cause) {
        super(message, cause);
    }

    public CryptoException(Exception cause) {
        super(cause);
    }
}
