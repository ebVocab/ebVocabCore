package de.ebuchner.vocab.tools.crypto;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CryptoTools {

    private final static Pattern VERSION_PATTERN = Pattern.compile("\\{\\{(\\d)\\}\\}(.*)");
    private static CryptoTools instance = null;
    private Map<Integer, Crypto> cryptoVersions = new HashMap<Integer, Crypto>();

    private CryptoTools() {
        cryptoVersions.put(0, new CryptoV0());
        cryptoVersions.put(1, new CryptoV1());
        cryptoVersions.put(2, new CryptoV2());
    }

    public static CryptoTools getInstance() {
        if (instance == null)
            instance = new CryptoTools();
        return instance;
    }

    public int version() {
        int result = new CryptoV0().version();
        for (int version : cryptoVersions.keySet()) {
            if (version > result)
                result = version;
        }
        return result;
    }

    public String decrypt(String encrypted) {
        Matcher m = VERSION_PATTERN.matcher(encrypted);

        Crypto crypto = new CryptoV0();
        String stringToDecrypt = encrypted;

        try {
            if (m.matches()) {

                int version = Integer.parseInt(m.group(1));
                Crypto tmpCrypto = cryptoVersions.get(version);
                if (tmpCrypto != null) {
                    crypto = tmpCrypto;
                    stringToDecrypt = m.group(2);
                }
            }
        } catch (Exception e) {

        }

        return crypto.decrypt(stringToDecrypt);
    }

    public String encrypt(String unencrypted) {
        Crypto crypto = cryptoVersions.get(version());
        return String.format("{{%d}}%s", crypto.version(), crypto.encrypt(unencrypted));
    }

}
