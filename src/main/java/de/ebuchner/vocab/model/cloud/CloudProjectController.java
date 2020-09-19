package de.ebuchner.vocab.model.cloud;

import de.ebuchner.vocab.config.ProjectInfo;
import de.ebuchner.vocab.model.VocabBaseController;
import de.ebuchner.vocab.model.nui.WindowType;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CloudProjectController extends VocabBaseController implements CloudConstants {

    private static final Logger LOGGER = Logger.getLogger(CloudProjectController.class.getName());
    private final CloudProjectWindowBehaviour cloudProjectWindow;

    public CloudProjectController(CloudProjectWindowBehaviour cloudProjectWindow) {
        this.cloudProjectWindow = cloudProjectWindow;
    }

    public void onRefreshProjects() {
        CloudModel cloudModel = CloudModel.getOrCreateCloudModel();
        onOpenWindowType(WindowType.CLOUD_LOGIN_WINDOW);
        if (!cloudModel.hasValues())
            return;

        try {
            CloudResult result = CloudAccessFactory.createNew().doFetchProjectList(cloudModel.createAccessParameters());
            if (result.getRemoteResult().getType() != CloudResult.RemoteResult.Type.PROJECT_LIST)
                return;
            cloudProjectWindow.updateProjectList(result.getRemoteResult().getRemoteProjectList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public void onCheckoutProject(File targetDirectory, ProjectItem selectedItem) {
        try {
            ProjectInfo projectInfo = new ProjectInfo(targetDirectory);
            CloudModel cloudModel = CloudModel.getOrCreateCloudModel();
            CloudAccessFactory.createNew().doDownloadAllProjectFiles(
                    cloudModel.createAccessParameters(),
                    projectInfo
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }
}
