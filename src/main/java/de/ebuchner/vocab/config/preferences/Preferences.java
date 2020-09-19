package de.ebuchner.vocab.config.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Preferences {

    private static final Logger LOGGER = Logger.getLogger(Preferences.class.getName());
    private File preferenceFile;
    private PreferenceValueList preferenceValueList = new PreferenceValueList();

    public Preferences(File preferenceFile) {
        this.preferenceFile = preferenceFile;
        loadPreferences();
    }

    private void loadPreferences() {
        preferenceValueList.clear();
        if (!preferenceFile.exists())
            return;
        try {
            try {
                System.out.println("Loading preferences from " + preferenceFile.getCanonicalPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            FileInputStream inputStream = new FileInputStream(preferenceFile);
            try {
                preferenceValueList = new PreferencesDecoder().decode(inputStream);
            } finally {
                inputStream.close();
            }

        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.toString(), e);
        }
    }

    public void savePreferences() {
        if (!preferenceFile.getParentFile().exists()) {
            if (!preferenceFile.getParentFile().mkdirs())
                throw new RuntimeException("Could not create " + preferenceFile.getParentFile());
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(preferenceFile);
            try {
                new PreferencesEncoder().encode(outputStream, preferenceValueList);
            } finally {
                outputStream.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PreferenceValueList getPreferenceValueList() {
        return preferenceValueList;
    }
}
