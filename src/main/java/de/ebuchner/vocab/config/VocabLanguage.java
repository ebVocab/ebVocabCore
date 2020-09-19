package de.ebuchner.vocab.config;

public class VocabLanguage {
    private final String code;
    private final String displayName;
    private final boolean knownTemplate;


    public VocabLanguage(String code, String displayName, boolean knownTemplate) {
        this.code = code;
        this.displayName = displayName;
        this.knownTemplate = knownTemplate;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isKnownTemplate() {
        return knownTemplate;
    }
}
