package de.ebuchner.vocab.tools.crypto;

interface Crypto {
    String decrypt(String encrypted);

    String encrypt(String unencrypted);

    int version();
}
