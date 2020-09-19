package de.ebuchner.vocab.model.editor;

import java.io.File;

public class SavedFile {
    private final long saveTimestamp;
    private final File file;

    public SavedFile(File file) {
        this(file, System.currentTimeMillis());
    }

    public SavedFile(File file, long saveTimestamp) {
        this.saveTimestamp = saveTimestamp;
        this.file = file;
    }

    public long getSaveTimestamp() {
        return saveTimestamp;
    }

    public File getFile() {
        return file;
    }
}
