package de.ebuchner.vocab.tools.crypto;

/**
 * Simple encryption (character shift)
 */
class CryptoV1 implements Crypto {

    public String decrypt(String encrypted) {
        StringBuilder result = new StringBuilder();
        for (char c : encrypted.toCharArray()) {
            result.append((char) ((c - 4) / 2));
        }
        return result.toString();
    }

    public String encrypt(String unencrypted) {
        StringBuilder result = new StringBuilder();
        for (char c : unencrypted.toCharArray()) {
            result.append((char) (c * 2 + 4));
        }
        return result.toString();
    }

    public int version() {
        return 1;
    }
}
