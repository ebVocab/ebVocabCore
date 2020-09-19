package de.ebuchner.vocab.model.editor;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.fields.Field;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;

import java.util.List;

public class EntryInEdit {
    private EntryInEditWindowBehaviour editorWindow;
    private EditorFileModelBehaviour fileModel;
    private List<Field> editFields = Config.instance().fieldListEditable();
    private String currentIDInEdit;
    private EditorAction currentAction = EditorAction.NONE;

    public EntryInEdit(EditorFileModelBehaviour fileModel, EntryInEditWindowBehaviour editorWindow) {
        this.fileModel = fileModel;
        this.editorWindow = editorWindow;
    }

    /**
     * EditorUI is about to being closed
     */
    public boolean acceptAndLeaveEditingMode() {
        if (currentAction == EditorAction.NONE)
            return true;

        VocabEntry entryFromUI = null;

        // entries which are still blank are discarded automatically
        boolean discardAutomatically = editorWindow.entryFromUI().fieldCount() == 0;

        if (!discardAutomatically) {
            entryFromUI = editorWindow.entryFromUI();
            boolean missingFields = hasMissingFields(entryFromUI);
            if (missingFields) {
                editorWindow.sendMessageFieldMissing();
                return false; // currentAction doesn't change
            }
        }

        // change action BEFORE updating model and UI
        EditorAction previousAction = currentAction;
        currentAction = EditorAction.NONE;

        if (entryFromUI != null) {
            if (previousAction == EditorAction.ADDING_NEW)
                fileModel.addEntry(entryFromUI);
            else
                fileModel.updateEntry(currentIDInEdit, entryFromUI);
        }

        editorWindow.entryToUI(null);

        return true;
    }

    private boolean hasMissingFields(VocabEntry entryFromUI) {
        boolean missingFields = false;
        for (Field field : editFields) {
            String value = entryFromUI.getFieldValue(field.name());
            if (!field.optional() && value == null)
                missingFields = true;
        }
        return missingFields;
    }

    public void startEditingNewEntry() {
        currentAction = EditorAction.ADDING_NEW;
        currentIDInEdit = null;
        editorWindow.entryToUI(new VocabEntry());
    }

    public void startEditingExistingEntry(VocabEntry entry) {
        currentAction = EditorAction.EDIT_EXISTING;
        currentIDInEdit = entry.getId();

        editorWindow.entryToUI(entry);
    }

    public void revert() {
        editorWindow.entryToUI(null);
        acceptAndLeaveEditingMode();
    }

    public boolean isActionNone() {
        return currentAction == EditorAction.NONE;
    }

    private enum EditorAction {
        EDIT_EXISTING, ADDING_NEW, NONE
    }


}
