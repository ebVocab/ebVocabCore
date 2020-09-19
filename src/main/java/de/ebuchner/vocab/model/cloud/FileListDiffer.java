package de.ebuchner.vocab.model.cloud;

import java.util.ArrayList;
import java.util.List;

public class FileListDiffer {
    private final FileList localList;
    private final FileList remoteList;
    private List<FileListAction> actions = new ArrayList<FileListAction>();

    public FileListDiffer() {
        this(new FileList(), new FileList());
    }

    public FileListDiffer(FileList localList, FileList remoteList) {
        this.localList = localList;
        this.remoteList = remoteList;

        doCompare();
    }

    public FileListDiffer(FileList localList, FileList remoteList, List<FileListAction> actions) {
        this.localList = localList;
        this.remoteList = remoteList;
        this.actions = actions;
    }

    public static FileListDiffer createWithActions(FileListDiffer differ, List<FileListAction> actions) {
        return new FileListDiffer(
                differ.localList, differ.remoteList, actions
        );

    }

    private void doCompare() {
        findLocalOnly();
        findServerOnly();
        findChanged();
    }

    private void findServerOnly() {
        for (FileItem remoteItem : remoteList.getFileItems()) {
            if (localList.findByTemplate(remoteItem) == null)
                actions.add(new FileListAction(FileListAction.ActionType.SERVER_ONLY, null, remoteItem));
        }
    }

    private void findChanged() {
        for (FileItem localItem : localList.getFileItems()) {
            FileItem remoteItem = remoteList.findByTemplate(localItem);
            if (remoteItem == null)
                continue;
            if (remoteItem.getLastModified() > localItem.getLastModified())
                actions.add(new FileListAction(FileListAction.ActionType.SERVER_NEWER, localItem, remoteItem));
            else if (remoteItem.getLastModified() < localItem.getLastModified())
                actions.add(new FileListAction(FileListAction.ActionType.LOCAL_NEWER, localItem, remoteItem));
        }
    }

    private void findLocalOnly() {
        for (FileItem localItem : localList.getFileItems()) {
            if (remoteList.findByTemplate(localItem) == null)
                actions.add(new FileListAction(FileListAction.ActionType.LOCAL_ONLY, localItem, null));
        }
    }

    public List<FileListAction> getActions() {
        return actions;
    }

    public void mergeWith(FileListDiffer otherDiffer) {
        mergeWith(otherDiffer.getActions());
    }

    private void mergeWith(List<FileListAction> otherActions) {
        for (FileListAction otherAction : otherActions) {
            FileListAction existingAction = findActionSimilarTo(otherAction);
            if (existingAction == null) {
                actions.add(otherAction);
                continue;
            }
        }
    }

    private FileListAction findActionSimilarTo(FileListAction otherAction) {
        for (FileListAction action : actions) {
            if (action.similarTo(otherAction))
                return action;
        }
        return null;
    }
}
