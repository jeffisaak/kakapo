package kakapo.crypto;

import com.goterl.lazycode.lazysodium.utils.Key;
import com.goterl.lazycode.lazysodium.utils.KeyPair;
import kakapo.crypto.exception.*;

public interface ICryptoService {

    KeyPair generateSigningKeyPair() throws KeyGenerationException;

    KeyPair generateKeyExchangeKeypair();

    Key generateGroupKey();

    byte[] calculateSharedSecret(Key mySecretKey, Key preKeyPublicKey);

    EncryptionResult encryptGroupKey(Key groupKey, Key sharedSecret) throws EncryptFailedException;

    byte[] decryptGroupKey(Key sharedSecret, byte[] groupKeyNonce, byte[] encryptedGroupKey)
            throws DecryptFailedException;

    EncryptionResult encryptShareData(byte[] data, Key groupSecret) throws EncryptFailedException;

    byte[] decryptShareData(byte[] encryptedData, byte[] nonce, Key groupSecret) throws DecryptFailedException;

    HashAndEncryptResult encryptSigningKey(Key signingSecretKey, String password)
            throws EncryptFailedException;

    byte[] decryptSigningKey(String password, String salt, String nonce, byte[] encryptedSigningKey)
            throws DecryptFailedException;

    byte[] signPreKey(Key preKeyPublicKey, Key signingSecretKey) throws SignMessageException;

    byte[] verifyPreKey(byte[] signedPreKeyPublicKey, Key signingPublicKey)
            throws SignatureVerificationFailedException;

}
