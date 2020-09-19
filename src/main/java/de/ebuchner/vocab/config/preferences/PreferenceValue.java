package de.ebuchner.vocab.config.preferences;

public interface PreferenceValue {
    String asString();

    void fromString(String stringValue);
}

