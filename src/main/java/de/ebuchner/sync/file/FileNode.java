package de.ebuchner.sync.file;

import de.ebuchner.sync.SyncNode;

import java.io.File;
import java.io.IOException;

public class FileNode implements SyncNode {

    private final File file;
    private final FileNode parentNode;

    public FileNode(FileNode parentNode, File file) {
        this.parentNode = parentNode;
        this.file = file;
    }

    @Override
    public String toString() {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public DirectoryNode asContainer() {
        throw new UnsupportedOperationException(this + " is not a container");
    }

    @Override
    public FileNode getParentNode() {
        return parentNode;
    }

    public File getFile() {
        return file;
    }
}
