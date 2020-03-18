package kakapo.crypto;

import com.goterl.lazycode.lazysodium.LazySodiumJava;
import com.goterl.lazycode.lazysodium.SodiumJava;
import com.goterl.lazycode.lazysodium.exceptions.SodiumException;
import com.goterl.lazycode.lazysodium.interfaces.PwHash;
import kakapo.crypto.exception.HashingException;

public class HashService {

    private LazySodiumJava _lazySodium;

    public HashService() {
        _lazySodium = new LazySodiumJava(new SodiumJava());
    }

    public String hashPassword(String password) throws HashingException {
        try {
            return _lazySodium.cryptoPwHashStr(password, PwHash.OPSLIMIT_MODERATE, PwHash.MEMLIMIT_MODERATE);
        } catch (SodiumException e) {
            throw new HashingException(e);
        }
    }

    public boolean verifyHash(String hash, String password) {
        return _lazySodium.cryptoPwHashStrVerify(hash, password);
    }
}
