package kakapo.crypto.exception;

public class SignatureVerificationErrorException extends CryptoException {

    public SignatureVerificationErrorException(String message) {
        super(message);
    }

    public SignatureVerificationErrorException(String message, Exception cause) {
        super(message, cause);
    }

    public SignatureVerificationErrorException(Exception cause) {
        super(cause);
    }
}
