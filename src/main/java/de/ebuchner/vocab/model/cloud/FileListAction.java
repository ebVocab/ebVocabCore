package de.ebuchner.vocab.model.cloud;

public class FileListAction {
    private final ActionType actionType;
    private final FileItem localItem;
    private final FileItem remoteItem;

    public FileListAction(ActionType actionType, FileItem localItem, FileItem remoteItem) {
        this.actionType = actionType;
        this.localItem = localItem;
        this.remoteItem = remoteItem;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public FileItem getLocalItem() {
        return localItem;
    }

    public FileItem getRemoteItem() {
        return remoteItem;
    }

    public boolean similarTo(FileListAction otherAction) {
        return sameFileItem(localItem, otherAction.getLocalItem()) && sameFileItem(remoteItem, otherAction.getRemoteItem());
    }

    private boolean sameFileItem(FileItem localItem, FileItem otherLocalItem) {
        if (localItem == null && otherLocalItem == null)
            return true;

        if (localItem == null || otherLocalItem == null)
            return false;

        return localItem.getFileName().equals(otherLocalItem.getFileName()) &&
                localItem.getRelativePath().equals(otherLocalItem.getRelativePath());
    }

    public enum ActionType {
        SERVER_ONLY, SERVER_NEWER, LOCAL_NEWER, LOCAL_ONLY
    }
}
