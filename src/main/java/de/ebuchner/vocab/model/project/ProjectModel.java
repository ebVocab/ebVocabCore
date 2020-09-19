package de.ebuchner.vocab.model.project;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.model.nui.platform.UIPlatformFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProjectModel {

    private static ProjectModel instance = null;
    private ProjectBean projectBean;
    private List<ProjectModelListener> listeners = new ArrayList<ProjectModelListener>();

    private ProjectModel() {
        projectBean = UIPlatformFactory.getUIPlatform().getProjectBean();
    }

    public static ProjectModel getInstance() {
        if (instance == null)
            instance = new ProjectModel();
        return instance;
    }

    private void fireModelChanged() {
        for (ProjectModelListener listener : listeners) {
            listener.projectDirectoryChanged();
        }
    }

    public void changeProjectDirectory(File selectedProjectDirectory) {
        try {
            projectBean.setCurrentHomeDirectory(selectedProjectDirectory.getCanonicalPath());
            projectBean.getRecentHomeDirectories().add(selectedProjectDirectory.getCanonicalPath());
            if (Config.projectInitialized())
                projectBean.getRecentHomeDirectories().add(
                        Config.instance().getProjectInfo().getProjectDirectory().getCanonicalPath()
                );
            projectBean.saveProjectBean();

            fireModelChanged();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File getProjectDirectory() {
        if (projectBean.getCurrentHomeDirectory() != null)
            return new File(projectBean.getCurrentHomeDirectory());
        return null;
    }

    public void addProjectModelListener(ProjectModelListener listener) {
        listeners.add(listener);
    }

    public void removeProjectModelListener(ProjectModelListener listener) {
        listeners.remove(listener);
    }
}
