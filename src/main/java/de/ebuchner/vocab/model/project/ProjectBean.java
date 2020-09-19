package de.ebuchner.vocab.model.project;

import java.util.HashSet;
import java.util.Set;

public abstract class ProjectBean {
    private String currentHomeDirectory;

    private Set<String> recentHomeDirectories = new HashSet<String>();

    public abstract void loadProjectBean();

    public abstract void saveProjectBean();

    public String getCurrentHomeDirectory() {
        return currentHomeDirectory;
    }

    public void setCurrentHomeDirectory(String currentHomeDirectory) {
        this.currentHomeDirectory = currentHomeDirectory;
    }

    public Set<String> getRecentHomeDirectories() {
        return recentHomeDirectories;
    }

    // will be used from XmlDecoder when loading project bean
    public void setRecentHomeDirectories(Set<String> recentHomeDirectories) {
        this.recentHomeDirectories = recentHomeDirectories;
    }

    public boolean isEmpty() {
        return currentHomeDirectory == null && recentHomeDirectories.isEmpty();
    }
}