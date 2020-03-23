package kakapo.crypto;

import com.goterl.lazycode.lazysodium.LazySodium;
import com.goterl.lazycode.lazysodium.exceptions.SodiumException;
import com.goterl.lazycode.lazysodium.interfaces.AEAD;
import com.goterl.lazycode.lazysodium.interfaces.PwHash;
import com.goterl.lazycode.lazysodium.interfaces.SecretBox;
import com.goterl.lazycode.lazysodium.interfaces.Sign;
import com.goterl.lazycode.lazysodium.utils.Key;
import com.goterl.lazycode.lazysodium.utils.KeyPair;
import kakapo.crypto.exception.*;

import java.nio.charset.StandardCharsets;

public class LibSodiumCrypto {

    private LazySodium _lazySodium;

    public LibSodiumCrypto(LazySodium lazySodium) {
        _lazySodium = lazySodium;
    }

    public KeyPair generateSigningKeyPair() throws KeyGenerationException {
        try {
            return _lazySodium.cryptoSignKeypair();
        } catch (SodiumException e) {
            throw new KeyGenerationException(e);
        }
    }

    public KeyPair generateKeyExchangeKeypair() {
        return _lazySodium.cryptoKxKeypair();
    }

    public Key generateGroupKey() {
        return _lazySodium.cryptoSecretBoxKeygen();
    }

    public byte[] calculateSharedSecret(Key mySecretKey, Key preKeyPublicKey) {
        byte[] sharedSecret = new byte[32];
        _lazySodium.cryptoScalarMult(sharedSecret,
                mySecretKey.getAsBytes(),
                preKeyPublicKey.getAsBytes());
        return sharedSecret;
    }

    public EncryptionResult encryptGroupKey(Key groupKey, Key sharedSecret) throws EncryptFailedException {
        return encrypt(groupKey.getAsBytes(), sharedSecret.getAsBytes());
    }

    public byte[] decryptGroupKey(Key sharedSecret, byte[] groupKeyNonce, Key encryptedGroupKey)
            throws DecryptFailedException {
        return decrypt(sharedSecret.getAsBytes(), groupKeyNonce, encryptedGroupKey.getAsBytes());
    }

    public EncryptionResult encryptShareData(byte[] data, Key groupSecret) throws EncryptFailedException {
        return encrypt(data, null, groupSecret.getAsBytes());
    }

    public byte[] decryptShareData(byte[] encryptedData, byte[] nonce, Key groupSecret) throws DecryptFailedException {
        return decrypt(encryptedData, null, nonce, groupSecret.getAsBytes());
    }

    public byte[] signPreKey(Key preKeyPublicKey, Key signingSecretKey) throws SignMessageException {
        return sign(preKeyPublicKey.getAsBytes(), signingSecretKey);
    }

    public byte[] verifyPreKey(byte[] signedPreKeyPublicKey, Key signingPublicKey)
            throws SignatureVerificationFailedException {
        return verify(signedPreKeyPublicKey, signingPublicKey);
    }

    // -------

    private HashResult hash(String input) {
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        return hash(inputBytes);
    }

    private HashResult hash(byte[] input) {

        // Allocate hash byte array and generate a random salt.
        byte[] hash = new byte[256];
        byte[] salt = _lazySodium.randomBytesBuf(PwHash.SALTBYTES);

        // Hash the password and salt to get an encryption key.
        PwHash.Lazy pwHash = (PwHash.Lazy) _lazySodium;
        _lazySodium.cryptoPwHash(hash,
                hash.length,
                input,
                input.length,
                salt,
                PwHash.OPSLIMIT_INTERACTIVE,
                PwHash.MEMLIMIT_INTERACTIVE,
                PwHash.Alg.getDefault());

        // Return the result.
        return new HashResult(hash, salt);
    }

    private EncryptionResult encrypt(byte[] key, byte[] message) throws EncryptFailedException {

        // Allocate ciphertext byte array and generate a random nonce.
        byte[] ciphertext = new byte[SecretBox.MACBYTES + message.length];
        byte[] nonce = _lazySodium.randomBytesBuf(SecretBox.NONCEBYTES);

        // Encrypt.
        if (!_lazySodium.cryptoSecretBoxEasy(ciphertext, message, message.length, nonce, key)) {
            throw new EncryptFailedException("Encryption failed");
        }

        // Return the result.
        return new EncryptionResult(ciphertext, nonce);
    }

    private byte[] decrypt(byte[] key, byte[] nonce, byte[] ciphertext) throws DecryptFailedException {

        // Allocate plaintext byte array.
        byte[] plaintext = new byte[ciphertext.length - SecretBox.MACBYTES];

        // Decrypt.
        if (!_lazySodium.cryptoSecretBoxOpenEasy(plaintext, ciphertext, ciphertext.length, nonce, key)) {
            throw new DecryptFailedException("Decryption failed");
        }

        // Return the result.
        return plaintext;
    }

    private EncryptionResult encrypt(byte[] message, byte[] additionalData, byte[] key)
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
                additionalData != null ? additionalData.length : 0,
                null,
                nonce,
                key);
        if (!encryptionSuccessful) {
            throw new EncryptFailedException("Encryption failed");
        }

        return new EncryptionResult(ciphertext, nonce);
    }

    private byte[] decrypt(byte[] ciphertext, byte[] additionalData, byte[] nonce, byte[] key)
            throws DecryptFailedException {

        byte[] plaintext = new byte[ciphertext.length - AEAD.XCHACHA20POLY1305_IETF_ABYTES];
        long[] plaintextLength = new long[1];

        boolean decryptSuccessful = _lazySodium.cryptoAeadXChaCha20Poly1305IetfDecrypt(plaintext,
                plaintextLength,
                null,
                ciphertext,
                ciphertext.length,
                additionalData,
                additionalData != null ? additionalData.length : 0,
                nonce,
                key);
        if (!decryptSuccessful) {
            throw new DecryptFailedException("Decryption failed");
        }

        return plaintext;
    }

    private byte[] sign(byte[] message, Key secretKey) throws SignMessageException {
        byte[] signedKey = new byte[message.length + Sign.BYTES];
        boolean signSuccessful = _lazySodium.cryptoSign(signedKey,
                message,
                message.length,
                secretKey.getAsBytes());
        if (!signSuccessful) {
            throw new SignMessageException("Sign failed");
        }
        return signedKey;
    }

    private byte[] verify(byte[] signedMessage, Key publicKey) throws SignatureVerificationFailedException {
        byte[] message = new byte[signedMessage.length - Sign.BYTES];
        boolean verified = _lazySodium.cryptoSignOpen(message,
                signedMessage,
                signedMessage.length,
                publicKey.getAsBytes());
        if (!verified) {
            throw new SignatureVerificationFailedException("Verification failed");
        }
        return message;
    }

}
