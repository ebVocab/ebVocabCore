package de.ebuchner.vocab.model.editor;

import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class EntrySelection implements EditorFileModelBehaviour, EntrySelectionBehaviour {
    private EditorFileModelBehaviour fileModel;
    private VocabEntryList selectedEntries = new VocabEntryList();

    public EntrySelection(EditorFileModelBehaviour fileModel) {
        this.fileModel = fileModel;
    }

    // EditorFileModelBehaviour
    public File getFile() {
        return fileModel.getFile();
    }

    // EditorFileModelBehaviour
    public void openFile(File file) {
        fileModel.openFile(file);
        selectedEntries = new VocabEntryList();
    }

    // EditorFileModelBehaviour
    public boolean hasFile() {
        return fileModel.hasFile();
    }

    // EditorFileModelBehaviour
    public void clearFile() {
        fileModel.clearFile();
        selectedEntries = new VocabEntryList();
    }

    // EditorFileModelBehaviour
    public boolean isDirty() {
        return fileModel.isDirty();
    }

    // EditorFileModelBehaviour
    public boolean isEmpty() {
        return fileModel.isEmpty();
    }

    // EditorFileModelBehaviour
    public void saveFile(File file) {
        fileModel.saveFile(file);
    }

    // EditorFileModelBehaviour
    public void addEntry(VocabEntry entry) {
        fileModel.addEntry(entry);

        selectedEntries = new VocabEntryList();
        selectedEntries.addEntry(entry);
    }

    // EditorFileModelBehaviour
    public VocabEntry updateEntry(String id, VocabEntry entry) {
        VocabEntry modelEntry = fileModel.updateEntry(id, entry);

        selectedEntries = new VocabEntryList();
        selectedEntries.addEntry(modelEntry);

        return modelEntry;
    }

    // EditorFileModelBehaviour
    public int entryCount() {
        return fileModel.entryCount();
    }

    // EditorFileModelBehaviour
    public VocabEntry getEntry(int pos) {
        return fileModel.getEntry(pos);
    }

    // EditorFileModelBehaviour
    public void deleteEntries(List<VocabEntry> entriesToDelete) {
        selectedEntries = new VocabEntryList();

        int minPos = -1;
        for (VocabEntry entryToDelete : entriesToDelete) {
            int pos = fileModel.indexOf(entryToDelete);
            if (pos >= 0 && (minPos < 0 || pos < minPos))
                minPos = pos;
        }

        VocabEntry entryToSelect = null;
        if (minPos > 0)
            entryToSelect = fileModel.getEntry(minPos - 1);

        fileModel.deleteEntries(entriesToDelete);

        if (entryToSelect != null)
            selectedEntries.addEntry(entryToSelect);
    }


    // EntrySelectionBehaviour
    public int selectionCount() {
        return selectedEntries.entryCount();
    }

    // EntrySelectionBehaviour
    public Iterable<Integer> selectedModelEntryRows() {
        List<Integer> rows = new ArrayList<Integer>();
        int entryCount = fileModel.entryCount();

        for (int row = 0; row < entryCount; row++) {
            VocabEntry entry = fileModel.getEntry(row);
            if (selectedEntries.findEntry(entry.getId()) != null)
                rows.add(row);
        }

        return rows;
    }

    // EntrySelectionBehaviour
    public void updateSelectedEntries(List<VocabEntry> vocabEntries) {
        selectedEntries = new VocabEntryList();
        for (VocabEntry entry : vocabEntries) {
            selectedEntries.addEntry(entry);
        }
    }

    // EntrySelectionBehaviour
    public int allEntriesCount() {
        return fileModel.entryCount();
    }

    public List<VocabEntry> allEntries() {
        List<VocabEntry> result = new ArrayList<VocabEntry>();
        for (VocabEntry entry : selectedEntries.entries())
            result.add(entry);
        return result;
    }

    // EditorFileModelBehaviour
    public void moveEntry(VocabEntry entry, EditorFileModel.MoveEntryDirection direction) {
        fileModel.moveEntry(entry, direction);
    }

    // EntrySelectionBehaviour
    public void clearSelection() {
        selectedEntries = new VocabEntryList();
    }

    // EntrySelectionBehaviour
    public void addSelection(VocabEntry entry) {
        if (selectedEntries.findEntry(entry.getId()) != null)
            return;
        selectedEntries.addEntry(entry);
    }

    // EditorFileModelBehaviour
    public int indexOf(VocabEntry entry) {
        return fileModel.indexOf(entry);
    }

    // EditorFileModelBehaviour
    public VocabEntry findEntry(String id) {
        return fileModel.findEntry(id);
    }
}
