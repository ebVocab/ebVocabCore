package de.ebuchner.vocab.model.cloud;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.ProjectInfo;

import java.io.File;
import java.util.List;

import static de.ebuchner.vocab.model.cloud.LocalFileStrategy.DELETE;

public class CloudController implements CloudConstants {

    protected final CloudWindowBehaviour cloudWindow;
    private final String locale;

    public CloudController(CloudWindowBehaviour cloudWindow) {
        this.cloudWindow = cloudWindow;
        this.locale = Config.instance().getLocale();
    }

    public CloudResult doTransferCloud(String url, ProjectInfo projectInfo, FileListDiffer differ, CloudTransfer cloudTransfer) throws Exception {
        return doTransferNoNotify(url, projectInfo, differ, cloudTransfer);
    }

    protected CloudResult doTransferNoNotify(String url, ProjectInfo projectInfo, FileListDiffer differ, CloudTransfer cloudTransfer) throws Exception {
        return doTransferNoNotify(url, projectInfo, differ.getActions(), cloudTransfer);
    }

    protected CloudResult doTransferNoNotify(String url, ProjectInfo projectInfo, List<FileListAction> actions, CloudTransfer cloudTransfer) throws Exception {
        if (!checkPreconditions(actions, cloudTransfer))
            return null;

        CloudResult cloudResult = new CloudResult(projectInfo);
        actionLoop:
        for (FileListAction action : actions) {
            if (cloudTransfer == CloudTransfer.UPLOAD) {
                switch (action.getActionType()) {
                    case SERVER_ONLY:
                        if (!doRemoveRemoteFile(url, projectInfo, cloudResult, action.getRemoteItem()))
                            break actionLoop;
                        break;
                    case LOCAL_ONLY:
                    case LOCAL_NEWER:
                    case SERVER_NEWER:
                        if (!doUploadFile(url, projectInfo, cloudResult, action.getLocalItem()))
                            break actionLoop;
                        break;
                }
            } else if (cloudTransfer == CloudTransfer.DOWNLOAD) {
                switch (action.getActionType()) {
                    case LOCAL_ONLY:
                        switch (shouldDeleteLocalOnlyFiles()) {
                            case DELETE:
                                if (!doRemoveLocalFile(projectInfo, cloudResult, action.getLocalItem()))
                                    break actionLoop;
                                break;
                            case UPLOAD:
                                if (!doUploadFile(url, projectInfo, cloudResult, action.getLocalItem()))
                                    break actionLoop;
                                break;
                        }
                        break;
                    case LOCAL_NEWER:
                    case SERVER_NEWER:
                    case SERVER_ONLY:
                        if (!doDownloadFile(url, projectInfo, cloudResult, action.getRemoteItem()))
                            break actionLoop;
                        break;
                }
            }
        }

        return cloudResult;
    }

    protected LocalFileStrategy shouldDeleteLocalOnlyFiles() {
        return DELETE;
    }

    private boolean doRemoveLocalFile(ProjectInfo projectInfo, CloudResult cloudResult, FileItem remoteItem) {
        File localFile = localFile(projectInfo, remoteItem);
        if (!localFile.exists())
            return true;

        boolean result = localFile.delete();
        if (!result)
            cloudResult.getLocalResult().deleteError(localFile);
        else
            cloudResult.getLocalResult().deletedOne();

        return result;
    }

    private boolean checkPreconditions(List<FileListAction> actions, CloudTransfer cloudTransfer) {
        for (FileListAction action : actions) {
            switch (action.getActionType()) {
                case SERVER_NEWER:
                    if (cloudTransfer == CloudTransfer.UPLOAD)
                        return cloudWindow.confirmOverwrite(cloudTransfer);
                    break;
                case LOCAL_NEWER:
                    if (cloudTransfer == CloudTransfer.DOWNLOAD)
                        return cloudWindow.confirmOverwrite(cloudTransfer);
                    break;
            }
        }
        return true;
    }

    private boolean doRemoveRemoteFile(String url, ProjectInfo projectInfo, CloudResult cloudResult, FileItem item) throws Exception {
        return CloudAccessFactory.createNew().doRemoveRemoteFile(
                createAccessParameters(url),
                projectInfo,
                cloudResult,
                item
        );
    }

    private boolean doUploadFile(String url, ProjectInfo projectInfo, CloudResult cloudResult, FileItem item) throws Exception {
        File vocabFile = new File(
                new File(
                        projectInfo.getVocabDirectory(),
                        item.getRelativePath()
                ),
                item.getFileName()
        );
        if (usePostForFileUpload())
            return CloudAccessFactory.createNew().doPostFile(
                    createAccessParameters(url),
                    vocabFile,
                    projectInfo,
                    cloudResult,
                    item,
                    locale
            );
        else
            return CloudAccessFactory.createNew().doUploadFile(
                    createAccessParameters(url),
                    vocabFile,
                    projectInfo,
                    cloudResult,
                    item,
                    locale
            );
    }

    protected boolean usePostForFileUpload() {
        return false;
    }

    private boolean doFetchFileListFromServer(String url, ProjectInfo projectInfo, CloudResult cloudResult) throws Exception {
        return CloudAccessFactory.createNew().doFetchFileListFromServer(
                createAccessParameters(url),
                projectInfo,
                cloudResult
        );
    }

    public CloudResult doRefreshCloud(String url, ProjectInfo projectInfo) throws Exception {
        return doRefreshNoNotify(url, projectInfo);
    }

    public void doUpdateUI(ProjectInfo projectInfo, CloudResult cloudResult) {
        if (cloudResult.getRemoteResult().getDownloadCount() > 0 || cloudResult.getLocalResult().getDeleteCount() > 0)
            CloudModel.getOrCreateCloudModel().localFilesChanged();

        cloudWindow.updateResult(projectInfo, cloudResult);
    }

    protected CloudResult doRefreshNoNotify(String url, ProjectInfo projectInfo) throws Exception {
        CloudResult cloudResult = new CloudResult(projectInfo);
        doFetchFileListFromServer(url, projectInfo, cloudResult);
        return cloudResult;
    }

    private File localFile(ProjectInfo projectInfo, FileItem fileItem) {
        File subDir = new File(
                projectInfo.getVocabDirectory(),
                fileItem.getRelativePath()
        );
        return new File(subDir, fileItem.getFileName());
    }

    private boolean doDownloadFile(String url, ProjectInfo projectInfo, CloudResult cloudResult, FileItem item) throws Exception {
        return CloudAccessFactory.createNew().doDownloadFile(
                createAccessParameters(url),
                projectInfo,
                item,
                cloudResult
        );
    }

    public CloudResult doFetchProjectList(String url) throws Exception {
        return CloudAccessFactory.createNew().doFetchProjectList(createAccessParameters(url));
    }

    private CloudAccessParameters createAccessParameters(String url) {
        CloudAccessParameters accessParameters = new CloudAccessParameters();
        accessParameters.serverUrl = url;
        accessParameters.userName = cloudWindow.getUserName();
        accessParameters.secret = cloudWindow.getSecret();
        return accessParameters;
    }
}
