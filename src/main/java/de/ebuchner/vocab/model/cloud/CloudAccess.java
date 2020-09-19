package de.ebuchner.vocab.model.cloud;

import de.ebuchner.vocab.config.ProjectInfo;

import java.io.File;

public interface CloudAccess extends CloudConstants {

    CloudResult doFetchProjectList(CloudAccessParameters accessParameters) throws Exception;

    default void doDownloadAllProjectFiles(CloudAccessParameters accessParameters, ProjectInfo projectInfo) throws Exception {
        CloudResult cloudResult = new CloudResult();
        if (!doFetchFileListFromServer(accessParameters, projectInfo, cloudResult))
            return;

        if (cloudResult.getRemoteResult().getType() != CloudResult.RemoteResult.Type.FILE_LIST)
            return;

        for (FileItem fileItem : cloudResult.getRemoteResult().getRemoteFileList().getFileItems()) {
            if (!doDownloadFile(
                    accessParameters,
                    projectInfo,
                    fileItem,
                    cloudResult
            )
            )
                break;
        }
    }

    boolean doDownloadFile(
            CloudAccessParameters accessParameters,
            ProjectInfo projectInfo,
            FileItem item,
            CloudResult cloudResult) throws Exception;

    boolean doFetchFileListFromServer(CloudAccessParameters accessParameters, ProjectInfo projectInfo, CloudResult cloudResult) throws Exception;

    boolean doPostFile(
            CloudAccessParameters accessParameters,
            File file,
            ProjectInfo projectInfo,
            CloudResult cloudResult,
            FileItem item,
            String locale
    ) throws Exception;

    boolean doUploadFile(
            CloudAccessParameters accessParameters,
            File file,
            ProjectInfo projectInfo,
            CloudResult cloudResult,
            FileItem item,
            String locale) throws Exception;

    boolean doRemoveRemoteFile(
            CloudAccessParameters accessParameters,
            ProjectInfo projectInfo,
            CloudResult cloudResult,
            FileItem item) throws Exception;
}
