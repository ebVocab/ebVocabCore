package de.ebuchner.vocab.config;

import de.ebuchner.vocab.config.fields.Field;
import de.ebuchner.vocab.config.preferences.Preferences;

import java.io.File;
import java.util.List;

public interface ConfigBehaviour extends ConfigConstants {

    void dispose();

    String fontSample();

    String getLocale();

    String getSystemLocale();

    List<Field> fieldListEditable();

    List<Field> fieldListVisible();

    int getExtraTextFieldHeight();

    void resetPreferences();

    String currencyName();

    String currencyFormatterClassName();

    boolean isImagePreviewRequired();

    int getImageFontSize();

    String getFieldRenderer(String fieldName);

    String timezoneID();

    void saveState();

    File getDefaultFontFile();

    ProjectInfo getProjectInfo();

    String appIconResourceName();

    Preferences statistics() throws SystemConfigException;

    Preferences preferences() throws SystemConfigException;

    int extraFontHeight();
}
