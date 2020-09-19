package de.ebuchner.vocab.config;

import java.util.*;

/**
 * All known configurations
 */
public class VocabLanguages {

    private static final List<String> KNOWN_LANGUAGES = Arrays.asList(
            "ar",
            "de",
            "en",
            "es",
            "fr",
            "hi",
            "ja",
            "la",
            "pt",
            "ru",
            "ur",
            "zh"
    );

    private VocabLanguages() {

    }

    public static List<VocabLanguage> loadVocabLanguages() {

        List<VocabLanguage> vocabLanguages = new ArrayList<VocabLanguage>();
        Set<String> codes = new HashSet<String>();

        for (Locale locale : Locale.getAvailableLocales()) {
            String code = locale.getLanguage();
            if (codes.contains(code))
                continue;
            codes.add(code);
        }

        for (String code : codes) {
            // otherwise you get the variants like "English (Puerto Rico)"
            Locale locale = new Locale(code);
            vocabLanguages.add(
                    new VocabLanguage(
                            code,
                            locale.getDisplayName(),
                            KNOWN_LANGUAGES.contains(code)
                    )
            );
        }

        if (!codes.contains("la")) {
            String code = "la";
            String displayName = "Latin";
            if (Locale.getDefault().getLanguage().equals("de"))
                displayName = "Latein";
            vocabLanguages.add(
                    new VocabLanguage(
                            code,
                            displayName,
                            true
                    )
            );
        }

        if (!codes.contains("ur")) {
            String code = "ur";
            String displayName = "Urdu";
            vocabLanguages.add(
                    new VocabLanguage(
                            code,
                            displayName,
                            true
                    )
            );
        }

        return Collections.unmodifiableList(vocabLanguages);
    }
}
