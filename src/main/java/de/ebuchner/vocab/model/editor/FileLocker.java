package de.ebuchner.vocab.model.editor;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FileLocker {

    private Set<String> lockedFilesNames = new HashSet<String>();

    public boolean isLocked(File file) {
        return lockedFilesNames.contains(fileName(file));
    }

    public void lockFile(File file) {
        if (isLocked(file))
            throw new IllegalArgumentException("Already locked " + file);

        lockedFilesNames.add(fileName(file));
    }

    public void unLockFile(File file) {
        if (!isLocked(file))
            throw new IllegalArgumentException("Not locked " + file);

        lockedFilesNames.remove(fileName(file));
    }

    private String fileName(File file) {
        if (file == null)
            throw new IllegalArgumentException("file may not be null");
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
