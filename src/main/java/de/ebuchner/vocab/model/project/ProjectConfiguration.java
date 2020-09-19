package de.ebuchner.vocab.model.project;

import de.ebuchner.vocab.config.ConfigConstants;
import de.ebuchner.vocab.config.ProjectConfig;
import de.ebuchner.vocab.config.ProjectInfo;

import java.io.*;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectConfiguration {

    private static final Logger LOGGER = Logger.getLogger(ProjectConfiguration.class.getName());

    private ProjectConfiguration() {

    }

    public static URL configResourceURL(String configName) {
        URL res = ProjectConfiguration.class.getResource(
                ConfigConstants.PROJECT_CONFIGURATIONS_RESOURCE_PREFIX + configName + ".properties");
        if (res != null)
            return res;

        return ProjectConfiguration.class.getResource(
                ConfigConstants.PROJECT_CONFIGURATIONS_DEFAULT_RESOURCE_PREFIX + ".properties");
    }

    public static URL configImageURL(String configName) {
        URL imgUrl = ProjectConfiguration.class.getResource(
                ConfigConstants.PROJECT_CONFIGURATIONS_RESOURCE_PREFIX + configName + ".png");
        if (imgUrl != null)
            return imgUrl;

        return ProjectConfiguration.class.getResource(
                ConfigConstants.PROJECT_CONFIGURATIONS_DEFAULT_RESOURCE_PREFIX + ".png");
    }

    public static File configProperties(ProjectInfo projectInfo) {
        return new File(
                projectInfo.getSystemDirectory(),
                ConfigConstants.CONFIG_PROPERTIES_FILENAME
        );
    }

    public static String projectLocale(File projectDirectory) {
        ProjectInfo projectInfo = new ProjectInfo(projectDirectory);
        Properties properties = loadConfigProperties(projectInfo);
        return properties.getProperty(ConfigConstants.CONFIG_SELECTOR_KEY);
    }

    private static Properties loadConfigProperties(ProjectInfo projectInfo) {
        Properties properties = new Properties();

        try {
            InputStream inp = new FileInputStream(configProperties(projectInfo));
            try {
                properties.load(inp);
            } finally {
                inp.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

    private static void createConfigProperties(ProjectInfo projectInfo, String configurationName) {
        Properties properties = new Properties();
        properties.put(ConfigConstants.CONFIG_SELECTOR_KEY, configurationName);
        try {
            OutputStream out = new FileOutputStream(configProperties(projectInfo));
            try {
                properties.store(out, null);
            } finally {
                out.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValidProjectDirectory(File projectDirectory) {
        if (projectDirectory == null)
            return notValidProjectDirectory("null value parameter", null);
        if (!projectDirectory.exists())
            return notValidProjectDirectory("project directory does not exist: {0} ", projectDirectory);
        if (!projectDirectory.isDirectory())
            return notValidProjectDirectory("not a project directory: {0}", projectDirectory);

        ProjectInfo projectInfo = new ProjectInfo(projectDirectory);
        File propertyFile = configProperties(projectInfo);
        boolean valid = propertyFile.exists();
        return valid || notValidProjectDirectory("Project directory missing configuration file {0}", propertyFile);
    }

    private static boolean notValidProjectDirectory(String message, File file) {
        String fileName = "";
        if (file != null) {
            fileName = file.getAbsolutePath();
            try {
                fileName = file.getCanonicalPath();
            } catch (IOException e) {
                LOGGER.log(Level.INFO, e.toString(), e);
            }
        }

        System.out.println(MessageFormat.format(message, fileName));
        return false;
    }

    public static void startupWithProjectDirectory(File projectDirectory) {
        ProjectInfo projectInfo = new ProjectInfo(projectDirectory);
        Properties properties = loadConfigProperties(projectInfo);
        String configName = (String) properties.get(ConfigConstants.CONFIG_SELECTOR_KEY);

        ProjectConfig.initialize(
                projectInfo,
                configResourceURL(configName),
                configName
        );
    }

    public static void createProjectDir(File projectDirectory, String configurationName) {
        ProjectInfo projectInfo = new ProjectInfo(projectDirectory);
        File systemDir = projectInfo.getSystemDirectory();
        if (!systemDir.mkdirs())
            throw new RuntimeException("Could not create " + systemDir);
        // FileTools.setHidden(systemDir); -- is also used in mobile environment

        createConfigProperties(projectInfo, configurationName);
    }

    public static File defaultFontFile(File projectDirectory) {
        File fontDir = new File(projectDirectory, "font");
        if (!fontDir.exists() || !fontDir.isDirectory())
            return null;

        File theFontFile = null;
        File[] fontFiles = fontDir.listFiles();
        if (fontFiles != null) {
            for (File fontFile : fontFiles)
                theFontFile = fontFile;
        }

        return theFontFile;
    }
}
