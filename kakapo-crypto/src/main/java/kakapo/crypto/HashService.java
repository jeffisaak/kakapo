package kakapo.crypto;

import com.goterl.lazycode.lazysodium.LazySodiumJava;
import com.goterl.lazycode.lazysodium.SodiumJava;
import com.goterl.lazycode.lazysodium.interfaces.PwHash;
import kakapo.crypto.exception.HashingException;

import java.nio.charset.StandardCharsets;

public class HashService {

    private LazySodiumJava _lazySodium;

    public HashService() {
        _lazySodium = new LazySodiumJava(new SodiumJava());
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
