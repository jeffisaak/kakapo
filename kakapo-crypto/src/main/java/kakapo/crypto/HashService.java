package kakapo.crypto;

import com.goterl.lazycode.lazysodium.LazySodium;
import com.goterl.lazycode.lazysodium.interfaces.PwHash;
import kakapo.crypto.exception.HashingException;

import java.nio.charset.StandardCharsets;

public class HashService {

    private LazySodium _lazySodium;

    public HashService(LazySodium lazySodium) {
        _lazySodium = lazySodium;
    }

    public String hashPassword(String passwordString) throws HashingException {
        byte[] hash = new byte[PwHash.STR_BYTES];
        byte[] password = passwordString.getBytes(StandardCharsets.UTF_8);
        boolean hashSuccess = _lazySodium.cryptoPwHashStr(hash,
                password,
                password.length,
                PwHash.OPSLIMIT_MODERATE,
                PwHash.MEMLIMIT_MODERATE);
        if (!hashSuccess) {
            throw new HashingException("Password hash failed");
        }
        return new String(hash);
    }

    public boolean verifyHash(String hashString, String passwordString) {
        byte[] hash = hashString.getBytes(StandardCharsets.UTF_8);
        byte[] password = passwordString.getBytes(StandardCharsets.UTF_8);
        return _lazySodium.cryptoPwHashStrVerify(hash, password, password.length);
    }
}
