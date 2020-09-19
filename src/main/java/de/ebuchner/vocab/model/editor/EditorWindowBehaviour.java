package de.ebuchner.vocab.model.editor;

import java.io.File;

public interface EditorWindowBehaviour {

    EditorFileModelBehaviour decorateModel(EditorFileModelBehaviour fileModel);

    void setWindowTitle(File file);

    void setEntrySelection(EntrySelectionBehaviour entrySelection);

    void setSaveOptionEnabled(boolean enabled);

    void setSaveAsOptionEnabled(boolean enabled);

    boolean askToRevert();

    void removeSingleRowSelectionAndFireNoEvents();

    void sendMessageOpenFileFailed(Throwable t, File fileToOpen);

    void sendMessageFileLockFailed(File file);

    void sendMessageEmptyFile();

    File openFileDialog(File preferredDirectory, String extension);

    File saveFileDialog(File preferredDirectory, String extension);

    AskToSaveResult askToSave();

    void sendMessageClipboardEmpty();

    enum AskToSaveResult {
        DO_SAVE, DO_CANCEL, DO_IGNORE
    }
}
