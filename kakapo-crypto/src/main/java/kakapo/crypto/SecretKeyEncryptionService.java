package kakapo.crypto;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import kakapo.crypto.exception.DecryptFailedException;
import kakapo.crypto.exception.EncryptFailedException;
import kakapo.crypto.exception.KeyGenerationException;

public class SecretKeyEncryptionService {

    private static final String PROVIDER = "BC";
    private static final int IV_LENGTH = 16;
    private static final int PBE_ITERATION_COUNT = 100;
    private static final int PBE_KEY_LENGTH = 256;

    private static final String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "AES";

    /**
     * Encrypt a byte array using the specified password and salt; return the result in a byte
     * array.
     *
     * @param password
     * @param salt
     * @param cleartext
     * @return
     * @throws EncryptFailedException
     */
    public byte[] encryptToByteArray(String password, String salt, byte[] cleartext)
            throws KeyGenerationException, EncryptFailedException {
        SecretKey secretKey = buildSecretKey(password, salt);
        return encryptToByteArray(secretKey, cleartext);
    }

    /**
     * Decrypt a byte array using the specified password and salt; return the result in a byte
     * array.
     *
     * @param password
     * @param salt
     * @param ciphertext
     * @return
     * @throws EncryptFailedException
     */
    public byte[] decryptToByteArray(String password, String salt, byte[] ciphertext)
            throws KeyGenerationException, DecryptFailedException {
        SecretKey secretKey = buildSecretKey(password, salt);
        return decryptToByteArray(secretKey, ciphertext);
    }

    /**
     * Encrypt a byte array using the specified secret key; return the result in a byte array.
     *
     * @param secret
     * @param cleartext
     * @return
     * @throws EncryptFailedException
     */
    private byte[] encryptToByteArray(SecretKey secret, byte[] cleartext)
            throws EncryptFailedException {
        try {
            byte[] iv = generateIv();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
            encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, ivParameterSpec);
            byte[] ciphertext = encryptionCipher.doFinal(cleartext);

            ByteArrayOutputStream ciphertextOutputStream = new ByteArrayOutputStream();
            ciphertextOutputStream.write(iv);
            ciphertextOutputStream.write(ciphertext);
            ciphertextOutputStream.flush();
            ciphertextOutputStream.close();
            return ciphertextOutputStream.toByteArray();

        } catch (Exception e) {
            throw new EncryptFailedException("Encryption error", e);
        }
    }

    /**
     * Decrypt a byte array using the specified secret key; return the result in a byte array.
     *
     * @param secret
     * @param ciphertext
     * @return
     * @throws DecryptFailedException
     */
    private byte[] decryptToByteArray(SecretKey secret, byte[] ciphertext)
            throws DecryptFailedException {
        try {
            Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);

            ByteArrayOutputStream ivStream = new ByteArrayOutputStream();
            ivStream.write(ciphertext, 0, IV_LENGTH);

            ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            dataStream.write(ciphertext, IV_LENGTH, ciphertext.length - IV_LENGTH);

            byte[] iv = ivStream.toByteArray();
            byte[] data = dataStream.toByteArray();

            IvParameterSpec ivspec = new IvParameterSpec(iv);
            decryptionCipher.init(Cipher.DECRYPT_MODE, secret, ivspec);
            return decryptionCipher.doFinal(data);
        } catch (Exception e) {
            throw new DecryptFailedException("Decryption error", e);
        }
    }

    /**
     * Build a secret key from a password and salt.
     *
     * @param password
     * @param salt
     * @return
     * @throws KeyGenerationException
     */
    private SecretKey buildSecretKey(String password, String salt) throws KeyGenerationException {
        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(),
                    salt.getBytes(StandardCharsets.UTF_8),
                    PBE_ITERATION_COUNT,
                    PBE_KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGORITHM, PROVIDER);
            SecretKey tmp = factory.generateSecret(pbeKeySpec);
            return new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_ALGORITHM);
        } catch (Exception e) {
            throw new KeyGenerationException("Secret key error", e);
        }
    }

    private static byte[] generateIv() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH];
        random.nextBytes(iv);
        return iv;
    }
}