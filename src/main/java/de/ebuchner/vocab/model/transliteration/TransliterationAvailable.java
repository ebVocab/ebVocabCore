package de.ebuchner.vocab.model.transliteration;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.tools.AvailableCheck;

import java.net.URL;

public class TransliterationAvailable implements AvailableCheck {
    public boolean isAvailable() {
        return transliterationURL() != null;
    }

    public URL transliterationURL() {
        return TransliterationAvailable.class.getResource(
                Config.instance().getLocale() + "_transliteration.txt"
        );
    }
}
