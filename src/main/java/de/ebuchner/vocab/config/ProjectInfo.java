package de.ebuchner.vocab.config;

import de.ebuchner.vocab.model.nui.platform.UIPlatformFactory;
import de.ebuchner.vocab.tools.FileTools;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.MessageFormat;

public class ProjectInfo {
    private final File projectDirectory;
    private final File systemDirectory;
    private final File vocabDirectory;
    private final File autoSaveDirectory;

    public ProjectInfo(File projectDirectory) {
        this.projectDirectory = projectDirectory;
        this.vocabDirectory = initializeVocabDirectory(projectDirectory);
        this.systemDirectory = initializeSystemDirectory(projectDirectory);
        this.autoSaveDirectory = initializeAutoSaveDirectory();
    }

    private static File initializeSystemDirectory(File projectDirectory) {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            hostname = UIPlatformFactory.getUIPlatform().getType().toString().toLowerCase();
        }
        File oldSystemDir = new File(projectDirectory, MessageFormat.format(ConfigConstants.SYSTEM_DIR_NAME_OLD, hostname));
        File newSystemDir = new File(projectDirectory, ConfigConstants.SYSTEM_DIR_NAME);
        if (oldSystemDir.exists() && !newSystemDir.exists()) {
            try {
                FileTools.moveDir(oldSystemDir, newSystemDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return newSystemDir;
    }

    private static File initializeVocabDirectory(File projectDirectory) {
        return new File(projectDirectory, ConfigConstants.VOCAB_DIR_NAME);
    }

    private File initializeAutoSaveDirectory() {
        return new File(vocabDirectory, ConfigConstants.AUTO_SAVE_DIRECTORY);
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public File getSystemDirectory() {
        return systemDirectory;
    }

    public File getVocabDirectory() {
        return vocabDirectory;
    }

    public String getName() {
        return projectDirectory.getName();
    }

    // hint: is not automatically created, because is not necessary on all platforms
    public File getAutoSaveDirectory() {
        return autoSaveDirectory;
    }

    @Override
    public String toString() {
        return String.format("ProjectInfo: %s", getName());
    }
}
