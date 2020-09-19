package de.ebuchner.vocab.model.alphabet;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.ConfigConstants;
import de.ebuchner.vocab.tools.AvailableCheck;

import java.net.URL;

public class AlphabetAvailable implements AvailableCheck {
    public boolean isAvailable() {
        return alphabetResource(Config.instance().getLocale()) != null;
    }

    public URL alphabetResource(String locale) {
        return AlphabetAvailable.class.getResource(
                locale + "_alphabet." + ConfigConstants.FILE_EXTENSION
        );
    }


}
