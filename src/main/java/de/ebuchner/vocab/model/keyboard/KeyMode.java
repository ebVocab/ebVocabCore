package de.ebuchner.vocab.model.keyboard;

public class KeyMode {
    private String locale;
    private KeyModifier modifier;
    private String displayText;
    private String generateText;

    public KeyMode(String locale) {
        this.locale = locale;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getGenerateText() {
        return generateText;
    }

    public void setGenerateText(String generateText) {
        this.generateText = generateText;
    }

    public String getLocale() {
        return locale;
    }

    public KeyModifier getModifier() {
        return modifier;
    }

    public void setModifier(KeyModifier modifier) {
        this.modifier = modifier;
    }
}
