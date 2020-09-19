package de.ebuchner.vocab.tools.crypto;

/**
 * No encryption
 */
class CryptoV0 implements Crypto {

    public String decrypt(String encrypted) {
        return encrypted;
    }

    public String encrypt(String unencrypted) {
        return unencrypted;
    }

    public int version() {
        return 0;
    }
}
