package de.ebuchner.vocab.model.project;

import java.io.File;

public interface ProjectWindowBehaviour {
    void sendNewProjectDirInvalidMessage();

    void sendExistingProjectDirInvalidMessage();

    File doOpenProjectDirectory();

    void doClose();
}
