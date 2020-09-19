package de.ebuchner.vocab.model.keyboard;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.tools.AvailableCheck;

import java.net.URL;

public class KeyboardAvailable implements AvailableCheck {
    public boolean isAvailable() {
        String locale = Config.instance().getLocale();
        return keyMapURL(locale) != null;
    }

    URL keyMapURL(String locale) {
        return KeyboardAvailable.class.getResource("/de/ebuchner/vocab/model/keyboard/keyMap_" + locale + ".xml");
    }
}
