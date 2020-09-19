package de.ebuchner.vocab.model.cloud;

public class CloudUrl {

    private CloudUrl() {

    }

    public static String getFileUploadUrl(String url) {
        return url + "/fileUpload/fileUpload.php";
    }

    public static String getFileListUrl(String url) {
        return url.trim() + "/access/listFiles.php";
    }

    public static String getClearProjectUrl(String url) {
        return url + "/fileUpload/clearProject.php";
    }

    public static String getGetFileUrl(String url) {
        return url + "/access/getFile.php";
    }

    public static String getDeleteFileUrl(String url) {
        return url + "/fileUpload/deleteFile.php";
    }

    public static String getProjectListUrl(String url) {
        return url + "/access/listProjects.php";
    }

    public static String getFilePostUrl(String url) {
        return url + "/fileUpload/filePost.php";
    }
}
