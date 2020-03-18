package kakapo.crypto;

import com.goterl.lazycode.lazysodium.LazySodium;
import com.goterl.lazycode.lazysodium.LazySodiumJava;
import com.goterl.lazycode.lazysodium.SodiumJava;
import com.goterl.lazycode.lazysodium.exceptions.SodiumException;
import com.goterl.lazycode.lazysodium.interfaces.AEAD;
import com.goterl.lazycode.lazysodium.interfaces.Box;
import com.goterl.lazycode.lazysodium.interfaces.KeyDerivation;
import com.goterl.lazycode.lazysodium.interfaces.Sign;
import com.goterl.lazycode.lazysodium.utils.Key;
import com.goterl.lazycode.lazysodium.utils.KeyPair;
import com.goterl.lazycode.lazysodium.utils.SessionPair;
import kakapo.crypto.exception.*;

import java.util.ArrayList;
import java.util.Collection;

public class PublicKeyEncryptionService {

    private LazySodium _lazySodium;

    public PublicKeyEncryptionService(LazySodium lazySodium) {
        _lazySodium = lazySodium;
    }

    public KeyPair generateSigningKeyPair() throws KeyGenerationException {
        try {
            return _lazySodium.cryptoSignKeypair();
        } catch (SodiumException e) {
            throw new KeyGenerationException(e);
        }
    }

    public KeyPair generateKeyExchangeKeyPair() {
        return _lazySodium.cryptoKxKeypair();
    }

    public byte[] generateClientSessionKey(KeyPair clientKeyPair, Key serverPublicKey) throws KeyGenerationException {
        try {
            SessionPair clientSessionPair = _lazySodium.cryptoKxClientSessionKeys(clientKeyPair.getPublicKey(),
                    clientKeyPair.getSecretKey(),
                    serverPublicKey);
            return clientSessionPair.getRx();
        } catch (SodiumException e) {
            throw new KeyGenerationException(e);
        }
    }

    public byte[] generateServerSessionKey(KeyPair serverKeyPair, Key clientPublicKey) throws KeyGenerationException {
        try {
            SessionPair serverSessionPair = _lazySodium.cryptoKxServerSessionKeys(serverKeyPair.getPublicKey(),
                    serverKeyPair.getSecretKey(),
                    clientPublicKey);
            return serverSessionPair.getTx();
        } catch (SodiumException e) {
            throw new KeyGenerationException(e);
        }
    }

    public Collection<Key> deriveSubkeys(Key masterKey, int count) throws KeyGenerationException {
        Collection<Key> result = new ArrayList<>();
        for (int ii = 0; ii < count; ii++) {
            try {
                Key derivedKey = _lazySodium.cryptoKdfDeriveFromKey(KeyDerivation.BLAKE2B_BYTES_MAX,
                        ii,
                        "session_",
                        masterKey);
                result.add(derivedKey);
            } catch (SodiumException e) {
                throw new KeyGenerationException(e);
            }
        }
        return result;
    }

    public byte[] sign(Key keyExchangePublicKey, Key signingSecretKey) throws SignMessageException {
        return sign(keyExchangePublicKey.getAsBytes(), signingSecretKey);
    }

    public byte[] sign(byte[] message, Key signingSecretKey) throws SignMessageException {
        byte[] signedKey = new byte[message.length + Sign.BYTES];
        boolean signSuccessful = _lazySodium.cryptoSign(signedKey,
                message,
                message.length,
                signingSecretKey.getAsBytes());
        if (!signSuccessful) {
            throw new SignMessageException("Sign failed");
        }
        return signedKey;
    }

    public byte[] verify(byte[] signedMessage, Key signingPublicKey) throws SignatureVerificationFailedException {
        byte[] message = new byte[signedMessage.length - Sign.BYTES];
        boolean verified = _lazySodium.cryptoSignOpen(message,
                signedMessage,
                signedMessage.length,
                signingPublicKey.getAsBytes());
        if (!verified) {
            throw new SignatureVerificationFailedException("Verification failed");
        }
        return message;
    }

    public EncryptionResult encrypt(byte[] message, byte[] additionalData, byte[] key)
            throws EncryptFailedException {

        byte[] ciphertext = new byte[message.length + AEAD.XCHACHA20POLY1305_IETF_ABYTES];
        long[] ciphertextLength = new long[1];
        byte[] nonce = _lazySodium.randomBytesBuf(AEAD.XCHACHA20POLY1305_IETF_NPUBBYTES);

        // Encrypt.
        boolean encryptionSuccessful = _lazySodium.cryptoAeadXChaCha20Poly1305IetfEncrypt(ciphertext,
                ciphertextLength,
                message,
                message.length,
                additionalData,
                additionalData.length,
                null,
                nonce,
                key);
        if (!encryptionSuccessful) {
            throw new EncryptFailedException("Encryption failed");
        }

        return new EncryptionResult(ciphertext, nonce);
    }

    public byte[] decrypt(byte[] ciphertext, byte[] additionalData, byte[] nonce, byte[] key)
            throws DecryptFailedException {

        byte[] plaintext = new byte[ciphertext.length - AEAD.XCHACHA20POLY1305_IETF_ABYTES];
        long[] plaintextLength = new long[1];

        boolean decryptSuccessful = _lazySodium.cryptoAeadXChaCha20Poly1305IetfDecrypt(plaintext,
                plaintextLength,
                null,
                ciphertext,
                ciphertext.length,
                additionalData,
                additionalData.length,
                nonce,
                key);
        if (!decryptSuccessful) {
            throw new DecryptFailedException("Decryption failed");
        }

        return plaintext;
    }

    public byte[] signAndEncrypt(byte[] message, Key signingSecretKey, Key encryptionPublicKey) {
        // Sign.
        byte[] signedMessage = new byte[Sign.BYTES + message.length];
        _lazySodium.cryptoSign(signedMessage, message, message.length, signingSecretKey.getAsBytes());

        // Encrypt.
        byte[] ciphertext = new byte[Box.SEALBYTES + signedMessage.length];
        _lazySodium.cryptoBoxSeal(ciphertext, signedMessage, signedMessage.length, encryptionPublicKey.getAsBytes());

        return ciphertext;
    }

    public byte[] decryptAndVerify(byte[] ciphertext, KeyPair encryptionKeyPair, Key signingPublicKey) {
        // Decrypt.
        byte[] plaintext = new byte[ciphertext.length - Box.SEALBYTES];
        _lazySodium.cryptoBoxSealOpen(plaintext,
                ciphertext,
                ciphertext.length,
                encryptionKeyPair.getPublicKey().getAsBytes(),
                encryptionKeyPair.getSecretKey().getAsBytes());

        // Verify.
        byte[] verifiedPlaintext = new byte[plaintext.length - Sign.BYTES];
        _lazySodium.cryptoSignOpen(verifiedPlaintext, plaintext, plaintext.length, signingPublicKey.getAsBytes());

        return verifiedPlaintext;
    }
}
