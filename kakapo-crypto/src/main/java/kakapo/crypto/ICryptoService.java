package kakapo.crypto;

import com.goterl.lazycode.lazysodium.utils.Key;
import com.goterl.lazycode.lazysodium.utils.KeyPair;
import kakapo.crypto.exception.*;

public interface ICryptoService {

    public KeyPair generateSigningKeyPair() throws KeyGenerationException;

    public KeyPair generateKeyExchangeKeypair();

    public Key generateGroupKey();

    public byte[] calculateSharedSecret(Key mySecretKey, Key preKeyPublicKey);

    public EncryptionResult encryptGroupKey(Key groupKey, Key sharedSecret) throws EncryptFailedException;

    public byte[] decryptGroupKey(Key sharedSecret, byte[] groupKeyNonce, Key encryptedGroupKey)
            throws DecryptFailedException;

    public EncryptionResult encryptShareData(byte[] data, Key groupSecret) throws EncryptFailedException;

    public byte[] decryptShareData(byte[] encryptedData, byte[] nonce, Key groupSecret) throws DecryptFailedException;

    public byte[] signPreKey(Key preKeyPublicKey, Key signingSecretKey) throws SignMessageException;

    public byte[] verifyPreKey(byte[] signedPreKeyPublicKey, Key signingPublicKey)
            throws SignatureVerificationFailedException;

}
