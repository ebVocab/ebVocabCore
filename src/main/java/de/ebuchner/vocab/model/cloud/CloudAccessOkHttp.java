package de.ebuchner.vocab.model.cloud;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.ProjectInfo;
import de.ebuchner.vocab.model.io.VocabIOHelper;
import okhttp3.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Logger;

public class CloudAccessOkHttp implements CloudAccess {

    private static final Logger logger = Logger.getLogger(CloudAccessOkHttp.class.getName());

    private final OkHttpClient client = new OkHttpClient();

    private boolean evaluateResponse(CloudResult cloudResult, Response response) {
        return cloudResult.getRemoteResult().evaluateHTTPResponse(response.code());
    }

    @Override
    public CloudResult doFetchProjectList(CloudAccessParameters accessParameters) throws Exception {
        CloudResult cloudResult = new CloudResult();

        Request request = new Request.Builder()
                .url(CloudUrl.getProjectListUrl(accessParameters.serverUrl))
                .post(new FormBody.Builder()
                        .add(USER_NAME_PARAM, accessParameters.userName)
                        .add(SECRET_PARAM, accessParameters.secret)
                        .build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!evaluateResponse(cloudResult, response))
                return cloudResult;

            cloudResult.getRemoteResult().remoteProjectListResult(
                    new ProjectList(response.body().string())
            );
            return cloudResult;
        }
    }

    @Override
    public boolean doDownloadFile(CloudAccessParameters accessParameters, ProjectInfo projectInfo, FileItem item, CloudResult cloudResult) throws Exception {
        File localFile = localFile(projectInfo, item);
        File targetSubDir = localFile.getParentFile();
        if (!targetSubDir.exists())
            if (!targetSubDir.mkdirs()) {
                cloudResult.getLocalResult().createError(targetSubDir);
                return false;
            }

        Request request = new Request.Builder()
                .url(CloudUrl.getGetFileUrl(accessParameters.serverUrl))
                .post(new FormBody.Builder()
                        .add(USER_NAME_PARAM, accessParameters.userName)
                        .add(SECRET_PARAM, accessParameters.secret)
                        .add(PROJECT_NAME_PARAM, projectInfo.getName())
                        .add(RELATIVE_PATH_PARAM, item.getRelativePath())
                        .add(FILE_NAME_PARAM, item.getFileName())
                        .build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!evaluateResponse(cloudResult, response))
                return false;

            FileOutputStream outputStream = new FileOutputStream(localFile);
            outputStream.write(response.body().bytes());
            outputStream.close();

            cloudResult.getRemoteResult().downloadedOne();
            return true;
        }
    }

    @Override
    public boolean doFetchFileListFromServer(CloudAccessParameters accessParameters, ProjectInfo projectInfo, CloudResult cloudResult) throws Exception {
        Request request = new Request.Builder()
                .url(CloudUrl.getFileListUrl(accessParameters.serverUrl))
                .post(new FormBody.Builder()
                        .add(USER_NAME_PARAM, accessParameters.userName)
                        .add(SECRET_PARAM, accessParameters.secret)
                        .add(PROJECT_NAME_PARAM, projectInfo.getName())
                        .build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!evaluateResponse(cloudResult, response))
                return false;

            cloudResult.getRemoteResult().remoteFileListResult(
                    new FileList(response.body().string())
            );
            return true;
        }
    }

    @Override
    public boolean doPostFile(CloudAccessParameters accessParameters, File file, ProjectInfo projectInfo, CloudResult cloudResult, FileItem item, String locale) throws Exception {
        Request request = new Request.Builder()
                .url(CloudUrl.getFilePostUrl(accessParameters.serverUrl))
                .post(new FormBody.Builder()
                        .add(USER_NAME_PARAM, accessParameters.userName)
                        .add(SECRET_PARAM, accessParameters.secret)
                        .add(PROJECT_NAME_PARAM, projectInfo.getName())
                        .add(RELATIVE_PATH_PARAM, item.getRelativePath())
                        .add(LOCALE_PARAM, locale)
                        .add(LAST_MODIFIED_PARAM, String.valueOf(item.getLastModified()))
                        .add(FILE_CONTENT_PARAM, VocabIOHelper.asString(file))
                        .add(FILE_NAME_PARAM, item.getFileName())
                        .build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Post file: " + response.code());
            if (!evaluateResponse(cloudResult, response))
                return false;

            cloudResult.getRemoteResult().uploadedOne();
            if (Config.projectInitialized())
                CloudModel.getOrCreateCloudModel().uploadFinished();
            return true;
        }
    }

    @Override
    public boolean doUploadFile(CloudAccessParameters accessParameters, File file, ProjectInfo projectInfo, CloudResult cloudResult, FileItem item, String locale) throws Exception {
        Request request = new Request.Builder()
                .url(CloudUrl.getFileUploadUrl(accessParameters.serverUrl))
                .post(new MultipartBody.Builder()
                        .setType(MediaType.get("multipart/form-data"))
                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.get("application/octet-stream")))
                        .addFormDataPart(USER_NAME_PARAM, accessParameters.userName)
                        .addFormDataPart(SECRET_PARAM, accessParameters.secret)
                        .addFormDataPart(PROJECT_NAME_PARAM, projectInfo.getName())
                        .addFormDataPart(RELATIVE_PATH_PARAM, item.getRelativePath())
                        .addFormDataPart(LOCALE_PARAM, locale)
                        .addFormDataPart(LAST_MODIFIED_PARAM, String.valueOf(item.getLastModified()))
                        .build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!evaluateResponse(cloudResult, response))
                return false;

            cloudResult.getRemoteResult().uploadedOne();
            CloudModel.getOrCreateCloudModel().uploadFinished();
            return true;
        }
    }

    @Override
    public boolean doRemoveRemoteFile(CloudAccessParameters accessParameters, ProjectInfo projectInfo, CloudResult cloudResult, FileItem item) throws Exception {
        Request request = new Request.Builder()
                .url(CloudUrl.getDeleteFileUrl(accessParameters.serverUrl))
                .post(new FormBody.Builder()
                        .add(USER_NAME_PARAM, accessParameters.userName)
                        .add(SECRET_PARAM, accessParameters.secret)
                        .add(PROJECT_NAME_PARAM, projectInfo.getName())
                        .add(RELATIVE_PATH_PARAM, item.getRelativePath())
                        .add(FILE_NAME_PARAM, item.getFileName())
                        .build())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!evaluateResponse(cloudResult, response))
                return false;

            cloudResult.getRemoteResult().deletedOne();
            return true;
        }
    }

    private File localFile(ProjectInfo projectInfo, FileItem fileItem) {
        File subDir = new File(
                projectInfo.getVocabDirectory(),
                fileItem.getRelativePath()
        );
        return new File(subDir, fileItem.getFileName());
    }
}
