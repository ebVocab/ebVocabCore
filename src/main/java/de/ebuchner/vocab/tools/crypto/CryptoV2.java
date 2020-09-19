package de.ebuchner.vocab.tools.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.StringTokenizer;

class CryptoV2 implements Crypto {

    private static final String BLOWFISH = "Blowfish";
    private static final String ENCODING = "utf-8";
    private static final byte[] BYTES = new byte[]{
            0x17, 0x10, 0x63, (byte) 0xeb, (byte) 0xa0,
            0x32, 0x5f, 0x1a, (byte) 0xff, (byte) 0xa3,
            0x37, (byte) 0xf2, (byte) 0x99, 0x00, 0x01
    };
    private static final String SEPARATOR = "*";

    public String decrypt(String encrypted) {
        SecretKeySpec keySpec = new SecretKeySpec(BYTES, BLOWFISH);
        try {
            Cipher cipher = Cipher.getInstance(BLOWFISH);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return new String(
                    cipher.doFinal(
                            stringToByteArray(encrypted)
                    ),
                    ENCODING
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String unencrypted) {
        SecretKeySpec keySpec = new SecretKeySpec(BYTES, BLOWFISH);
        try {
            Cipher cipher = Cipher.getInstance(BLOWFISH);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return byteArrayToString(cipher.doFinal(unencrypted.getBytes(ENCODING)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String byteArrayToString(byte[] bytes) {
        if (bytes == null)
            throw new NullPointerException();
        StringBuilder buffer = new StringBuilder();
        for (byte aByte : bytes) {
            buffer.append(Byte.toString(aByte)).append(SEPARATOR);
        }
        return buffer.toString();
    }

    byte[] stringToByteArray(String string) {
        StringTokenizer tokenizer = new StringTokenizer(string, SEPARATOR);
        byte[] result = new byte[tokenizer.countTokens()];

        int pos = 0;
        while (tokenizer.hasMoreTokens()) {
            result[pos] = Byte.parseByte(tokenizer.nextToken());
            pos++;
        }

        return result;
    }

    public int version() {
        return 2;
    }
}
