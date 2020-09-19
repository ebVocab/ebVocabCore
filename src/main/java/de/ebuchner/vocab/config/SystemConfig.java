package de.ebuchner.vocab.config;

import de.ebuchner.vocab.config.preferences.Preferences;

import java.io.File;
import java.util.Currency;
import java.util.Locale;

public class SystemConfig extends Config {

    @Override
    public String appIconResourceName() {
        return APPLICATION_ICON_RESOURCE_NAME;
    }

    @Override
    public Preferences statistics() throws SystemConfigException {
        throw new SystemConfigException();
    }

    @Override
    public Preferences preferences() throws SystemConfigException {
        throw new SystemConfigException();
    }

    @Override
    public int extraFontHeight() {
        return 0;
    }

    @Override
    public String fontSample() {
        return "";
    }

    @Override
    public String getLocale() {
        return Locale.getDefault().getLanguage();
    }

    @Override
    public int getExtraTextFieldHeight() {
        return 0;
    }

    @Override
    public void resetPreferences() {

    }

    @Override
    public String currencyName() {
        return Currency.getInstance(getSystemLocale()).getDisplayName();
    }

    @Override
    public String currencyFormatterClassName() {
        throw new SystemConfigException();
    }

    @Override
    public boolean isImagePreviewRequired() {
        throw new SystemConfigException();
    }

    @Override
    public int getImageFontSize() {
        throw new SystemConfigException();
    }

    @Override
    public String getFieldRenderer(String fieldName) {
        throw new SystemConfigException();
    }

    @Override
    public String timezoneID() {
        throw new SystemConfigException();
    }

    @Override
    public void saveState() {

    }

    @Override
    public File getDefaultFontFile() {
        throw new SystemConfigException();
    }

    @Override
    public ProjectInfo getProjectInfo() {
        throw new SystemConfigException();
    }

}
