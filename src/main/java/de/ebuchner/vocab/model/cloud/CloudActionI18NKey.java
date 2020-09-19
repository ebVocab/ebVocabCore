package de.ebuchner.vocab.model.cloud;

public class CloudActionI18NKey {

    public static String findKeyFor(CloudTransfer cloudTransfer, FileListAction.ActionType actionType) {

        if (cloudTransfer == CloudTransfer.UPLOAD) {
            switch (actionType) {
                case SERVER_ONLY:
                    return "nui.cloud.action.upload.delete";
                case LOCAL_ONLY:
                    return "nui.cloud.action.upload.add";
                case SERVER_NEWER:
                    return "nui.cloud.action.upload.overwrite";
                case LOCAL_NEWER:
                    return "nui.cloud.action.upload.update";
            }
        } else if (cloudTransfer == CloudTransfer.DOWNLOAD) {
            switch (actionType) {
                case SERVER_ONLY:
                    return "nui.cloud.action.download.add";
                case LOCAL_ONLY:
                    return "nui.cloud.action.download.delete";
                case SERVER_NEWER:
                    return "nui.cloud.action.download.update";
                case LOCAL_NEWER:
                    return "nui.cloud.action.download.overwrite";
            }
        }
        throw new IllegalArgumentException(String.format("Unexpected value of CloudTransfer %s or ActionType %s", cloudTransfer.name(), actionType.name()));
    }
}
