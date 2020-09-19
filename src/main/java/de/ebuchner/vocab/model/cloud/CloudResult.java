package de.ebuchner.vocab.model.cloud;

import de.ebuchner.vocab.config.ProjectInfo;

import java.io.File;

public class CloudResult {

    private final ProjectInfo projectInfo;
    private final RemoteResult remoteResult;
    private final LocalResult localResult;

    public CloudResult(ProjectInfo projectInfo) {
        this.projectInfo = projectInfo;
        this.remoteResult = new RemoteResult();
        this.localResult = new LocalResult();
    }

    public CloudResult() {
        this(null);
    }

    public ProjectInfo getProjectInfo() {
        return projectInfo;
    }

    public RemoteResult getRemoteResult() {
        return remoteResult;
    }

    public LocalResult getLocalResult() {
        return localResult;
    }

    public static class LocalResult {
        private File errorFile;
        private int deleteCount = 0;
        private Type type = Type.LOCAL_OK;

        public void deleteError(File errorFile) {
            type = Type.DELETE_ERROR;
            this.errorFile = errorFile;
        }

        public void createError(File errorFile) {
            type = Type.CREATE_ERROR;
            this.errorFile = errorFile;
        }

        public Type getType() {
            return type;
        }

        public File getErrorFile() {
            return errorFile;
        }

        public void deletedOne() {
            deleteCount++;
        }

        public int getDeleteCount() {
            return deleteCount;
        }

        public enum Type {
            LOCAL_OK,
            DELETE_ERROR,
            CREATE_ERROR
        }
    }

    public static class RemoteResult {
        private static final int HTTP_OK = 200;
        private static final int HTTP_BAD_REQUEST = 400;
        private static final int HTTP_FORBIDDEN = 403;
        private static final int HTTP_INTERNAL_SERVER_ERROR = 500;

        private FileList remoteFileList;
        private ProjectList remoteProjectList;
        private Type type = Type.REMOTE_OK;
        private int uploadCount = 0;
        private int downloadCount = 0;
        private int deleteCount = 0;

        public static Type fromHttpStatusCode(int status) {
            Type type;
            switch (status) {
                case HTTP_OK:
                    type = Type.REMOTE_OK;
                    break;
                case HTTP_BAD_REQUEST:
                    type = Type.CLIENT_ERROR;
                    break;
                case HTTP_FORBIDDEN:
                    type = Type.AUTHENTICATION_ERROR;
                    break;
                case HTTP_INTERNAL_SERVER_ERROR:
                    type = Type.SERVER_ERROR;
                    break;
                default:
                    type = Type.OTHER_ERROR;
                    System.out.printf("Received status code %d%n", status);
                    break;
            }
            return type;
        }

        public ProjectList getRemoteProjectList() {
            return remoteProjectList;
        }

        public FileList getRemoteFileList() {
            return remoteFileList;
        }

        public Type getType() {
            return type;
        }

        public boolean evaluateHTTPResponse(int statusCode) {
            type = fromHttpStatusCode(statusCode);
            return type == Type.REMOTE_OK;
        }

        public void uploadedOne() {
            uploadCount++;
        }

        public void downloadedOne() {
            downloadCount++;
        }

        public void deletedOne() {
            deleteCount++;
        }

        public void remoteFileListResult(FileList remoteFileList) {
            type = Type.FILE_LIST;
            this.remoteFileList = remoteFileList;
        }

        public void remoteProjectListResult(ProjectList remoteProjectList) {
            type = Type.PROJECT_LIST;
            this.remoteProjectList = remoteProjectList;
        }

        public int getUploadCount() {
            return uploadCount;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public int getDeleteCount() {
            return deleteCount;
        }

        public FileListDiffer createDiffer(ProjectInfo projectInfo) {
            return new FileListDiffer(
                    new FileList(projectInfo),
                    remoteFileList
            );
        }

        public enum Type {
            AUTHENTICATION_ERROR, // HTTP_BAD_REQUEST
            CLIENT_ERROR, // HTTP_BAD_REQUEST
            SERVER_ERROR, // HTTP_INTERNAL_SERVER_ERROR
            REMOTE_OK, // HTTP_OK
            FILE_LIST, // HTTP_OK and FileList
            PROJECT_LIST,
            OTHER_ERROR
        }
    }

}
