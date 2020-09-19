package de.ebuchner.vocab.model.cloud;

import de.ebuchner.toolbox.i18n.I18NContext;
import de.ebuchner.vocab.config.ProjectInfo;
import de.ebuchner.vocab.nui.common.I18NLocator;

import java.util.Arrays;
import java.util.Collections;

public abstract class DefaultCloudWindowBehaviour implements CloudWindowBehaviour {

    private static final FileListDiffer EMPTY_DIFFER = new FileListDiffer();
    private I18NContext i18n = I18NLocator.locate();

    public final void updateResult(ProjectInfo projectInfo, CloudResult cloudResult) {
        String statusMessage;
        ProjectStatus projectStatus;
        switch (cloudResult.getLocalResult().getType()) {
            case CREATE_ERROR:
                statusMessage =
                        i18n.getString(
                                "nui.cloud.result.create.error",
                                Collections.singletonList(cloudResult.getLocalResult().getErrorFile())
                        );
                projectStatus = ProjectStatus.ERROR;
                break;
            case DELETE_ERROR:
                statusMessage =
                        i18n.getString(
                                "nui.cloud.result.delete.error",
                                Collections.singletonList(cloudResult.getLocalResult().getErrorFile())
                        );
                projectStatus = ProjectStatus.ERROR;
                break;
            case LOCAL_OK:
                switch (cloudResult.getRemoteResult().getType()) {
                    case AUTHENTICATION_ERROR:
                        statusMessage =
                                i18n.getString(
                                        "nui.cloud.result.authentication.error"
                                );
                        projectStatus = ProjectStatus.ERROR;
                        break;
                    case CLIENT_ERROR:
                    case SERVER_ERROR:
                    case OTHER_ERROR:
                        statusMessage =
                                i18n.getString(
                                        "nui.cloud.result.server.error",
                                        Collections.singletonList(cloudResult.getRemoteResult().getType())
                                );
                        projectStatus = ProjectStatus.ERROR;
                        break;
                    case REMOTE_OK:
                        statusMessage = i18n.getString("nui.cloud.result.ok");
                        projectStatus = ProjectStatus.NOTHING_CHANGED;
                        if (cloudResult.getRemoteResult().getUploadCount() > 0) {
                            statusMessage = statusMessage +
                                    i18n.getString(
                                            "nui.cloud.result.ok.uploaded",
                                            Collections.singletonList(cloudResult.getRemoteResult().getUploadCount())
                                    );
                            projectStatus = ProjectStatus.PROJECT_UPDATED;
                        }
                        if (cloudResult.getRemoteResult().getDownloadCount() > 0) {
                            statusMessage = statusMessage +
                                    i18n.getString(
                                            "nui.cloud.result.ok.downloaded",
                                            Collections.singletonList(cloudResult.getRemoteResult().getDownloadCount())
                                    );
                            projectStatus = ProjectStatus.PROJECT_UPDATED;
                        }
                        if (cloudResult.getLocalResult().getDeleteCount() > 0) {
                            statusMessage = statusMessage +
                                    i18n.getString(
                                            "nui.cloud.result.ok.locally.deleted",
                                            Arrays.asList(cloudResult.getLocalResult().getDeleteCount())
                                    );
                            projectStatus = ProjectStatus.PROJECT_UPDATED;
                        }
                        if (cloudResult.getRemoteResult().getDeleteCount() > 0) {
                            statusMessage = statusMessage +
                                    i18n.getString(
                                            "nui.cloud.result.ok.remote.deleted",
                                            Arrays.asList(cloudResult.getRemoteResult().getDeleteCount())
                                    );
                            projectStatus = ProjectStatus.PROJECT_UPDATED;
                        }
                        onNewDownloadDiffer(projectInfo, EMPTY_DIFFER);
                        onNewUploadDiffer(projectInfo, EMPTY_DIFFER);
                        break;
                    case FILE_LIST:
                        FileListDiffer differ = cloudResult.getRemoteResult().createDiffer(projectInfo);
                        if (differ.getActions().isEmpty()) {
                            statusMessage =
                                    i18n.getString(
                                            "nui.cloud.result.upload.differ.0"
                                    );
                            projectStatus = ProjectStatus.FILE_LIST_REFRESHED;
                        } else {
                            statusMessage =
                                    i18n.getString(
                                            "nui.cloud.result.upload.differ",
                                            Collections.singletonList(differ.getActions().size())
                                    );
                            projectStatus = ProjectStatus.FILE_LIST_REFRESHED;
                        }
                        onNewDownloadDiffer(projectInfo, differ);
                        onNewUploadDiffer(projectInfo, differ);
                        break;
                    default:
                        throw new RuntimeException("Unexpected result: " + cloudResult.getRemoteResult().getType());
                }
                break;
            default:
                throw new RuntimeException("Unexpected result: " + cloudResult.getLocalResult().getType());
        }
        onUpdateStatusMessage(projectInfo, statusMessage);
        onUpdateProjectStatus(projectInfo, projectStatus);
    }

    protected abstract void onUpdateProjectStatus(ProjectInfo projectInfo, ProjectStatus projectStatus);

    protected abstract void onUpdateStatusMessage(ProjectInfo projectInfo, String statusMessage);

    protected abstract void onNewUploadDiffer(ProjectInfo projectInfo, FileListDiffer differ);

    protected abstract void onNewDownloadDiffer(ProjectInfo projectInfo, FileListDiffer differ);
}
