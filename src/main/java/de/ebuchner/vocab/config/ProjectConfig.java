package de.ebuchner.vocab.config;

import de.ebuchner.vocab.config.preferences.Preferences;
import de.ebuchner.vocab.model.nui.platform.UIPlatformFactory;
import de.ebuchner.vocab.model.project.ProjectConfiguration;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;

public class ProjectConfig extends Config {
    private final ProjectInfo projectInfo;
    private final String locale;
    private Properties settings;
    private Preferences preferencesModel;
    private Preferences statisticsModel;

    private ProjectConfig(
            URL propertiesResource,
            ProjectInfo projectInfo,
            String locale
    ) {
        this.projectInfo = projectInfo;
        if (!projectInfo.getVocabDirectory().exists()) {
            if (!projectInfo.getVocabDirectory().mkdirs())
                throw new RuntimeException("Could not create " + projectInfo.getVocabDirectory());
        }
        this.locale = locale;

        settings = new Properties();
        try {
            InputStream inputStream = propertiesResource.openStream();
            try {
                settings.load(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ProjectConfig initialize(ProjectInfo projectInfo, URL configResource, String locale) {
        System.out.println("Loading configuration from " + configResource);

        ProjectConfig instance = new ProjectConfig(
                configResource,
                projectInfo,
                locale
        );

        instance.loadPreferences();
        instance.loadStatistics();

        Config.changeProjectConfig(instance);

        return instance;
    }

    private void loadPreferences() {
        File preferenceFile = getSystemFile(PREFERENCES_FILENAME_PATTERN);
        preferencesModel = new Preferences(preferenceFile);
    }

    private void loadStatistics() {
        File statisticsFile = getSystemFile(STATISTICS_FILENAME_PATTERN);
        statisticsModel = new Preferences(statisticsFile);
    }

    @Override
    public String appIconResourceName() {
        final String localeIconResourceName = "/de/ebuchner/vocab/config/templates/" + locale + ".png";
        URL imgResource = Config.class.getResource(localeIconResourceName);
        if (imgResource != null)
            return localeIconResourceName;
        return APPLICATION_ICON_RESOURCE_NAME;
    }

    @Override
    public ProjectInfo getProjectInfo() {
        return projectInfo;
    }

    File getSystemFile(String pattern) {
        return new File(
                projectInfo.getSystemDirectory(),
                MessageFormat.format(pattern, System.getProperty("user.name"))
        );
    }

    public Preferences statistics() {
        return statisticsModel;
    }

    public Preferences preferences() {
        return preferencesModel;
    }

    @Override
    public int extraFontHeight() {
        String heightString = settings.getProperty("common.extraTextFieldHeight");
        if (heightString == null)
            return 0;
        return Integer.parseInt(heightString);
    }

    @Override
    public void dispose() {
        saveState();
        super.dispose();
    }

    @Override
    public String fontSample() {
        return settings.getProperty("common.fontSample");
    }

    @Override
    public String getLocale() {
        return locale;
    }

    @Override
    public int getExtraTextFieldHeight() {
        String heightValue = settings.getProperty("common.extraTextFieldHeight");
        if (heightValue == null)
            return 0;
        return Integer.parseInt(heightValue);
    }

    @Override
    public void resetPreferences() {
        File prefFile = getSystemFile(PREFERENCES_FILENAME_PATTERN);
        if (prefFile.exists())
            if (!prefFile.delete()) {
                throw new RuntimeException("Could not delete " + prefFile);
            }
        this.preferencesModel = new Preferences(prefFile);
    }

    @Override
    public String currencyName() {
        return settings.getProperty("common.currency");
    }

    @Override
    public String currencyFormatterClassName() {
        return settings.getProperty("nui.currency.renderer");
    }

    @Override
    public boolean isImagePreviewRequired() {
        String booleanValue = settings.getProperty("common.imagePreview", "false");
        return Boolean.valueOf(booleanValue);
    }

    @Override
    public int getImageFontSize() {
        String intValue = settings.getProperty("editor.imageFontSize", "-1");
        return Integer.parseInt(intValue);
    }

    @Override
    public String getFieldRenderer(String fieldName) {
        String className = settings.getProperty("nui.practice.renderer." + fieldName);
        if (className != null) {
            className = MessageFormat.format(className, UIPlatformFactory.getUIPlatform().getType().toString().toLowerCase());
        }
        return className;
    }

    @Override
    public String timezoneID() {
        return settings.getProperty("common.timezone");
    }

    @Override
    public void saveState() {
        preferencesModel.savePreferences();
        statisticsModel.savePreferences();
    }

    @Override
    public File getDefaultFontFile() {
        return ProjectConfiguration.defaultFontFile(
                projectInfo.getProjectDirectory()
        );
    }
}
