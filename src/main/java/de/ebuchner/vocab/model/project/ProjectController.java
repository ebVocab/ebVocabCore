package de.ebuchner.vocab.model.project;

import de.ebuchner.vocab.model.VocabBaseController;
import de.ebuchner.vocab.model.nui.platform.UIPlatformFactory;
import de.ebuchner.vocab.nui.NuiDirector;
import de.ebuchner.vocab.tools.FileTools;

import java.io.File;

public class ProjectController extends VocabBaseController {

    private ProjectWindowBehaviour projectWindow;
    private ProjectModel projectModel;

    public ProjectController(ProjectWindowBehaviour projectWindow) {
        this.projectWindow = projectWindow;
        this.projectModel = ProjectModel.getInstance();
    }

    public void onWindowWasCreated() {

    }

    public void onUseProject(File selectedProjectDirectory) {
        onUseProjectAndDoNotClose(selectedProjectDirectory);
        handleProjectChanged();
    }

    public void onUseProjectAndDoNotClose(File selectedProjectDirectory) {
        projectModel.changeProjectDirectory(selectedProjectDirectory);
    }

    private void handleProjectChanged() {
        NuiDirector nuiDirector = UIPlatformFactory.getUIPlatform().getNuiDirector();
        nuiDirector.reboot();
    }

    public void onOpenProject() {
        File homeDirectory = projectWindow.doOpenProjectDirectory();
        if (homeDirectory == null)
            return;
        else if (!ProjectConfiguration.isValidProjectDirectory(homeDirectory)) {
            projectWindow.sendExistingProjectDirInvalidMessage();
            return;
        }

        onUseProject(homeDirectory);
    }

    public boolean onNewProject(File homeDirectory, String configurationName) {
        if (homeDirectory == null || (homeDirectory.exists() && !FileTools.isEmpty(homeDirectory))) {
            projectWindow.sendNewProjectDirInvalidMessage();
            return false;
        }
        ProjectConfiguration.createProjectDir(homeDirectory, configurationName);
        return true;
    }
}
