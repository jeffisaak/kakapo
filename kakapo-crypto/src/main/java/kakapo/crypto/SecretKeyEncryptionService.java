package kakapo.crypto;

import com.goterl.lazycode.lazysodium.LazySodium;
import com.goterl.lazycode.lazysodium.interfaces.PwHash;
import com.goterl.lazycode.lazysodium.interfaces.SecretBox;
import kakapo.crypto.exception.DecryptFailedException;
import kakapo.crypto.exception.EncryptFailedException;

import java.nio.charset.StandardCharsets;

public class SecretKeyEncryptionService {

    // We're using a zeroed out salt as we're not storing the password anywhere. The random nonce used
    // to perform the encryption will suffice.
    private static final byte[] SALT = new byte[PwHash.SALTBYTES];

    private LazySodium _lazySodium;

    public SecretKeyEncryptionService(LazySodium lazySodium) {
        _lazySodium = lazySodium;
    }

    /**
     * Encrypt a byte array using the specified password.
     *
     * @param passwordString
     * @param message
     * @return
     * @throws EncryptFailedException
     */
    public EncryptionResult encrypt(String passwordString, byte[] message)
            throws EncryptFailedException {

        // Convert password to byte array, allocate key byte array, and generate a random salt.
        byte[] password = passwordString.getBytes(StandardCharsets.UTF_8);
        byte[] key = new byte[256];
        // byte[] salt = _lazySodium.randomBytesBuf(PwHash.SALTBYTES);

        // Hash the password and salt to get an encryption key.
        _lazySodium.cryptoPwHash(key,
                key.length,
                password,
                password.length,
                SALT,
                PwHash.OPSLIMIT_INTERACTIVE,
                PwHash.MEMLIMIT_INTERACTIVE,
                PwHash.Alg.getDefault());

        // Allocate ciphertext byte array and generate a random nonce.
        byte[] ciphertext = new byte[SecretBox.MACBYTES + message.length];
        byte[] nonce = _lazySodium.randomBytesBuf(SecretBox.NONCEBYTES);

        // Encrypt.
        if (!_lazySodium.cryptoSecretBoxEasy(ciphertext, message, message.length, nonce, key)) {
            throw new EncryptFailedException("Encryption failed");
        }

        return new EncryptionResult(ciphertext, nonce);
    }

    /**
     * Decrypt a byte array using the specified password and nonce.
     *
     * @param passwordString
     * @param nonce
     * @param ciphertext
     * @return
     * @throws EncryptFailedException
     */
    public byte[] decrypt(String passwordString, byte[] nonce, byte[] ciphertext)
            throws DecryptFailedException {

        // Convert password to byte array and allocate key byte array.
        byte[] password = passwordString.getBytes(StandardCharsets.UTF_8);
        byte[] key = new byte[256];

        // Hash the password and salt to get an encryption key.
        _lazySodium.cryptoPwHash(key,
                key.length,
                password,
                password.length,
                SALT,
                PwHash.OPSLIMIT_INTERACTIVE,
                PwHash.MEMLIMIT_INTERACTIVE,
                PwHash.Alg.getDefault());

        // Allocate plaintext byte array.
        byte[] plaintext = new byte[ciphertext.length - SecretBox.MACBYTES];

        // Decrypt.
        if (!_lazySodium.cryptoSecretBoxOpenEasy(plaintext, ciphertext, ciphertext.length, nonce, key)) {
            throw new DecryptFailedException("Decryption failed");
        }

        return plaintext;
    }

}