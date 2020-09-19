package de.ebuchner.vocab.config.preferences;

public interface PreferencesSupport {
    void restoreFromPreferences(PreferenceValueList preferenceValues);

    void saveToPreferences(PreferenceValueList preferenceValues);
}
