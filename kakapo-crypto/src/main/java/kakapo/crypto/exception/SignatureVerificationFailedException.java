package kakapo.crypto.exception;

public class SignatureVerificationFailedException extends CryptoException {

    public SignatureVerificationFailedException(String message) {
        super(message);
    }
}
