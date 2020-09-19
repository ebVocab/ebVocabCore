package de.ebuchner.vocab.config.preferences;

import de.ebuchner.vocab.tools.crypto.CryptoTools;

public class PasswordValue extends StringValue {

    public PasswordValue() {
    }

    public void setValueFromUnencrypted(String unencrypted) {
        super.string = CryptoTools.getInstance().encrypt(unencrypted);
    }

    public String asUnencryptedString() {
        return CryptoTools.getInstance().decrypt(super.string);
    }

}
