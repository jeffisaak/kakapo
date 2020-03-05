package kakapo.crypto;

import kakapo.crypto.exception.*;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.BouncyGPG;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.algorithms.PGPHashAlgorithms;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.algorithms.PGPSymmetricEncryptionAlgorithms;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.generation.KeySpec;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.generation.KeySpecBuilder;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.generation.Passphrase;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.generation.type.RSAForEncryptionKeyType;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.generation.type.length.RsaLength;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.InMemoryKeyring;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.KeyringConfig;
import name.neuhalfen.projects.crypto.bouncycastle.openpgp.keys.keyrings.KeyringConfigs;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.util.io.Streams;

import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a layer around the Bouncy GPG package. Which is a little silly, as Bouncy GPG is a layer around Bouncycastle.
 * I suppose that's more a comment on how usable Bouncycastle is to us mere mortals than anything.
 */
public class PGPEncryptionService {

    static {
        BouncyGPG.registerProvider();
    }

    /**
     * Given a public key, return the key length.
     *
     * @param publicKey
     * @return
     * @throws CheckKeyLengthException
     */
    public int getKeyLength(byte[] publicKey) throws CheckKeyLengthException {
        try {
            // Build the keyring.
            InMemoryKeyring keyring = KeyringConfigs.forGpgExportedKeys(keyId -> new char[0]);
            keyring.addPublicKey(publicKey);
            PGPPublicKey pgpPublicKey = keyring.getPublicKeyRings().iterator().next().getPublicKey();
            return pgpPublicKey.getBitStrength();
        } catch (PGPException | IOException e) {
            throw new CheckKeyLengthException(e);
        }
    }

    /**
     * Generate a public/secret keypair using the specified key length, user id, and passphrase.
     *
     * @param keyLength
     * @param guid
     * @param passphrase
     * @return
     * @throws KeyGenerationException
     */
    public KeyringConfig generateKeyRings(RsaLength keyLength, String guid, String passphrase)
            throws KeyGenerationException {
        try {
            return generateKeyRingsInternal(keyLength, guid, passphrase);
        } catch (IOException | PGPException | InvalidAlgorithmParameterException |
                NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new KeyGenerationException(e);
        }
    }

    private KeyringConfig generateKeyRingsInternal(RsaLength keyLength, String guid, String passphrase)
            throws IOException,
            PGPException,
            NoSuchAlgorithmException,
            NoSuchProviderException,
            InvalidAlgorithmParameterException {

        KeySpec keySpec = KeySpecBuilder
                .newSpec(RSAForEncryptionKeyType.withLength(keyLength))
                .allowKeyToBeUsedForEverything()
                .withDetailedConfiguration()
                .withPreferredSymmetricAlgorithms(PGPSymmetricEncryptionAlgorithms.AES_256)
                .withPreferredHashAlgorithms(PGPHashAlgorithms.SHA_512)
                .withDefaultCompressionAlgorithms()
                .done();

        KeyringConfig keyringConfig = BouncyGPG
                .createKeyring()
                .withMasterKey(keySpec)
                .withPrimaryUserId(String.format("%1$s <%1$s>", guid))
                .withPassphrase(Passphrase.fromString(passphrase))
                .build();

        return keyringConfig;
    }

    /**
     * Serialize a given public/secret keypair into a couple of byte arrays and return them inside a KeyPair container
     * class.
     *
     * @param keyrings
     * @return
     * @throws KeyRingSerializationException
     */
    public KeyPair serializeKeyRings(KeyringConfig keyrings) throws KeyRingSerializationException {
        try {
            return serializeKeyRingsInternal(keyrings);
        } catch (IOException | PGPException e) {
            throw new KeyRingSerializationException(e);
        }
    }

    private KeyPair serializeKeyRingsInternal(KeyringConfig keyrings)
            throws IOException, PGPException {

        ByteArrayOutputStream secretKeyStream = new ByteArrayOutputStream();
        keyrings.getSecretKeyRings().encode(secretKeyStream);
        secretKeyStream.close();
        byte[] secretKey = secretKeyStream.toByteArray();

        ByteArrayOutputStream publicKeyStream = new ByteArrayOutputStream();
        keyrings.getPublicKeyRings().encode(publicKeyStream);
        publicKeyStream.close();
        byte[] publicKey = publicKeyStream.toByteArray();

        return new KeyPair(secretKey, publicKey);
    }

    /**
     * Decrypt a cipher text given a keypair and passphrase.
     *
     * @param cipherText
     * @param keyPair
     * @param passphrase
     * @return
     * @throws DecryptFailedException
     */
    public byte[] decrypt(byte[] cipherText,
                          KeyPair keyPair,
                          String passphrase) throws DecryptFailedException {
        try {
            return decryptInternal(cipherText, keyPair, passphrase, null, null);
        } catch (IOException | PGPException | NoSuchProviderException e) {
            throw new DecryptFailedException(e);
        }
    }

    /**
     * Decrypt and verify a signed cipher text given a keypair, passphrase, and source user id and public key.
     *
     * @param cipherText
     * @param keyPair
     * @param passphrase
     * @param sourceGuid
     * @param sourcePublicKey
     * @return
     * @throws DecryptFailedException
     */
    public byte[] decryptAndVerify(byte[] cipherText,
                                   KeyPair keyPair,
                                   String passphrase,
                                   String sourceGuid,
                                   byte[] sourcePublicKey) throws DecryptFailedException {
        try {
            return decryptInternal(cipherText, keyPair, passphrase, sourceGuid, sourcePublicKey);
        } catch (IOException | PGPException | NoSuchProviderException e) {
            throw new DecryptFailedException(e);
        }
    }

    private byte[] decryptInternal(byte[] cipherText,
                                   KeyPair keyPair,
                                   String passphrase,
                                   String sourceGuid,
                                   byte[] sourcePublicKey)
            throws IOException, PGPException, NoSuchProviderException {

        // Build the keyring.
        InMemoryKeyring keyring = KeyringConfigs.forGpgExportedKeys(keyId -> passphrase.toCharArray());
        keyring.addSecretKey(keyPair.getSecretKey());
        keyring.addPublicKey(keyPair.getPublicKey());
        if (sourcePublicKey != null) {
            keyring.addPublicKey(sourcePublicKey);
        }

        // Decrypt and optionally verify signature.
        final ByteArrayOutputStream plainStream = new ByteArrayOutputStream();
        InputStream cipherStream = null;
        BufferedOutputStream bufferedOut = null;
        InputStream plaintextStream = null;
        try {
            cipherStream = new ByteArrayInputStream(cipherText);
            bufferedOut = new BufferedOutputStream(plainStream);

            if (sourceGuid != null && sourcePublicKey != null) {
                plaintextStream = BouncyGPG
                        .decryptAndVerifyStream()
                        .withConfig(keyring)
                        .andRequireSignatureFromAllKeys(String.format("%1$s <%1$s>", sourceGuid))
                        .fromEncryptedInputStream(cipherStream);
            } else {
                plaintextStream = BouncyGPG
                        .decryptAndVerifyStream()
                        .withConfig(keyring)
                        .andIgnoreSignatures()
                        .fromEncryptedInputStream(cipherStream);
            }

            Streams.pipeAll(plaintextStream, bufferedOut);

        } finally {
            if (cipherStream != null) {
                cipherStream.close();
            }
            if (bufferedOut != null) {
                bufferedOut.close();
            }
            if (plaintextStream != null) {
                plaintextStream.close();
            }
        }

        plainStream.close();

        return plainStream.toByteArray();
    }

    /**
     * Encrypt and sign a given clear text byte array with the specified key pair, user id, passphrase, and set of
     * recipients.
     *
     * @param clearText
     * @param keyPair
     * @param guid
     * @param passphrase
     * @param recipients
     * @return
     * @throws EncryptFailedException
     */
    public byte[] encryptAndSign(byte[] clearText,
                                 KeyPair keyPair,
                                 String guid,
                                 String passphrase,
                                 PGPRecipient... recipients)
            throws EncryptFailedException {
        try {
            return encryptAndSignInternal(clearText, keyPair, guid, passphrase, recipients);
        } catch (IOException | PGPException | NoSuchAlgorithmException | SignatureException | NoSuchProviderException e) {
            throw new EncryptFailedException(e);
        }
    }

    private byte[] encryptAndSignInternal(byte[] clearText,
                                          KeyPair keyPair,
                                          String guid,
                                          String passphrase,
                                          PGPRecipient... recipients)
            throws IOException,
            PGPException,
            NoSuchAlgorithmException,
            SignatureException,
            NoSuchProviderException {

        // Start building the keyring.
        InMemoryKeyring keyring = KeyringConfigs.forGpgExportedKeys(keyId -> passphrase.toCharArray());
        keyring.addPublicKey(keyPair.getPublicKey());
        keyring.addSecretKey(keyPair.getSecretKey());

        // Add the public key(s) and build the array of recipient IDs.
        List<String> recipientIds = new ArrayList<>();
        for (PGPRecipient recipient : recipients) {
            try {
                keyring.addPublicKey(recipient.getPublicKey());
            } catch (IllegalArgumentException e) {
                // Probably because we've already added the key to the ring. Ignore.
            }
            String recipientId = String.format("%1$s <%1$s>", recipient.getGuid());
            if (!recipientIds.contains(recipientId)) {
                recipientIds.add(recipientId);
            }
        }

        // Encrypt.
        ByteArrayOutputStream cipherStream = new ByteArrayOutputStream();
        try (
                BufferedOutputStream bufferedCipherStream = new BufferedOutputStream(cipherStream);

                final OutputStream outputStream = BouncyGPG
                        .encryptToStream()
                        .withConfig(keyring)
                        .withStrongAlgorithms()
                        .toRecipients(recipientIds.toArray(new String[0]))
                        .andSignWith(String.format("%1$s <%1$s>", guid))
                        .binaryOutput()
                        .andWriteTo(bufferedCipherStream);

                final ByteArrayInputStream clearTextInputStream = new ByteArrayInputStream(clearText)
        ) {
            Streams.pipeAll(clearTextInputStream, outputStream);
        }
        cipherStream.close();

        return cipherStream.toByteArray();
    }

    /**
     * Produce a digital signature.
     *
     * @param messageDigest
     * @param keyPair
     * @param guid
     * @param passphrase
     * @return
     * @throws SignMessageException
     */
    public byte[] sign(byte[] messageDigest,
                       KeyPair keyPair,
                       String guid,
                       String passphrase)
            throws SignMessageException {
        try {
            return signInternal(messageDigest, keyPair, guid, passphrase);
        } catch (IOException | PGPException | NoSuchAlgorithmException | SignatureException | NoSuchProviderException e) {
            throw new SignMessageException(e);
        }
    }

    private byte[] signInternal(byte[] messageDigest,
                                KeyPair keyPair,
                                String guid,
                                String passphrase)
            throws IOException,
            PGPException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException {

        // Start building the keyring.
        InMemoryKeyring keyring = KeyringConfigs.forGpgExportedKeys(keyId -> passphrase.toCharArray());
        keyring.addPublicKey(keyPair.getPublicKey());
        keyring.addSecretKey(keyPair.getSecretKey());

        // Sign.
        ByteArrayOutputStream signatureStream = new ByteArrayOutputStream();
        try (
                BufferedOutputStream bufferedSignatureStream = new BufferedOutputStream(signatureStream);

                final OutputStream outputStream = BouncyGPG
                        .signToStream()
                        .withConfig(keyring)
                        .withStrongAlgorithms()
                        .andSignWith(String.format("%1$s <%1$s>", guid))
                        .armorAsciiOutput()
                        .andWriteTo(bufferedSignatureStream);

                final ByteArrayInputStream clearTextInputStream = new ByteArrayInputStream(messageDigest)
        ) {
            Streams.pipeAll(clearTextInputStream, outputStream);
        }
        signatureStream.close();

        return signatureStream.toByteArray();
    }

    /**
     * Verify a signature against a message digest.
     *
     * @param messageDigest
     * @param signature
     * @param sourceGuid
     * @param sourcePublicKey
     * @throws SignatureVerificationErrorException  If an error occurred while verifying the signature.
     * @throws SignatureVerificationFailedException If the signature verification failed; that is, the signature is not
     *                                              correct.
     */
    public void verify(byte[] messageDigest,
                       byte[] signature,
                       String sourceGuid,
                       byte[] sourcePublicKey)
            throws SignatureVerificationErrorException,
            SignatureVerificationFailedException {
        try {
            verifyInternal(messageDigest, signature, sourceGuid, sourcePublicKey);
        } catch (IOException | PGPException | NoSuchProviderException e) {
            e.printStackTrace();
            throw new SignatureVerificationErrorException(e);
        }
    }

    private void verifyInternal(byte[] messageDigest,
                                byte[] signature,
                                String sourceGuid,
                                byte[] sourcePublicKey)
            throws SignatureVerificationFailedException, IOException, PGPException, NoSuchProviderException {

        // Build the keyring.
        InMemoryKeyring keyring = KeyringConfigs.forGpgExportedKeys(keyId -> new char[0]);
        keyring.addPublicKey(sourcePublicKey);

        // Verify the signature.
        final ByteArrayOutputStream plainStream = new ByteArrayOutputStream();
        InputStream signedStream = null;
        BufferedOutputStream bufferedOut = null;
        InputStream messageDigestStream = null;
        try {
            signedStream = new ByteArrayInputStream(signature);
            bufferedOut = new BufferedOutputStream(plainStream);

            messageDigestStream = BouncyGPG
                    .verifyStream()
                    .withConfig(keyring)
                    .andRequireSignatureFromAllKeys(String.format("%1$s <%1$s>", sourceGuid))
                    .fromSignedInputStream(signedStream);

            Streams.pipeAll(messageDigestStream, bufferedOut);

        } finally {
            if (signedStream != null) {
                signedStream.close();
            }
            if (bufferedOut != null) {
                bufferedOut.close();
            }
            if (messageDigestStream != null) {
                messageDigestStream.close();
            }
        }

        plainStream.close();

        // Compare the verified signature against the message digest.
        if (!Arrays.equals(plainStream.toByteArray(), messageDigest)) {
            throw new SignatureVerificationFailedException("Decrypted signature does not match message digest");
        }
    }
}
