package de.ebuchner.vocab.model.editor;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.model.cloud.CloudModel;
import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;

import java.io.File;
import java.util.List;

public class EditorFileModel implements EditorFileModelBehaviour {

    private File file;
    private boolean dirty;
    private VocabEntryList entryList = new VocabEntryList();

    public File getFile() {
        return file;
    }

    public void openFile(File file) {
        this.file = file;
        dirty = false;
        loadEntries();
    }

    private void loadEntries() {
        entryList = VocabIOHelper.fromFile(file);
        dirty = false;
    }

    public boolean hasFile() {
        return file != null;
    }

    public void clearFile() {
        file = null;
        entryList = new VocabEntryList();
        dirty = false;
    }

    public boolean isDirty() {
        return dirty;
    }

    public boolean isEmpty() {
        return entryList.entryCount() == 0;
    }

    public void saveFile(File file) {
        boolean imagePreviewRequired = Config.instance().isImagePreviewRequired();
        if (imagePreviewRequired)
            VocabIOHelper.toFileWithImage(file, entryList);
        else
            VocabIOHelper.toFile(file, entryList);
        this.file = file;
        EditorModel.getOrCreateEditorModel().fileSaved();
        CloudModel.getOrCreateCloudModel().localFilesChanged();
        dirty = false;
    }

    public void addEntry(VocabEntry entry) {
        entryList.addEntry(entry);
        dirty = true;
    }

    public VocabEntry updateEntry(String id, VocabEntry entry) {
        VocabEntry modelEntry = entryList.findEntry(id);
        if (modelEntry == null)
            throw new RuntimeException("internal error: entry with id " + id + " not found in current editor model");

        if (modelEntry.updateFieldsFrom(entry)) {
            dirty = true;
        }

        return modelEntry;
    }

    public int entryCount() {
        return entryList.entryCount();
    }

    public VocabEntry getEntry(int pos) {
        return entryList.getEntry(pos);
    }

    public void deleteEntries(List<VocabEntry> entriesToDelete) {
        VocabEntryList newEntryList = new VocabEntryList();
        for (VocabEntry entry : entryList.entries()) {
            if (entriesToDelete.contains(entry)) {
                dirty = true;
                continue;
            }
            newEntryList.addEntry(entry);
        }
        entryList = newEntryList;
    }

    public void moveEntry(VocabEntry anEntry, MoveEntryDirection direction) {
        int newIndex = entryList.indexOf(anEntry);

        if (direction == MoveEntryDirection.UP)
            newIndex = newIndex - 1;
        else
            newIndex = newIndex + 2;

        int index = 0;
        VocabEntryList newEntryList = new VocabEntryList();
        for (VocabEntry entry : entryList.entries()) {
            if (!entry.getId().equals(anEntry.getId())) {
                if (index == newIndex) {
                    newEntryList.addEntry(anEntry);
                }
                newEntryList.addEntry(entry);
            }
            index++;
        }
        if (index == newIndex)
            newEntryList.addEntry(anEntry);

        entryList = newEntryList;
        dirty = true;
    }

    public int indexOf(VocabEntry entry) {
        return entryList.indexOf(entry);
    }

    public VocabEntry findEntry(String id) {
        return entryList.findEntry(id);
    }

    public static enum MoveEntryDirection {
        UP, DOWN
    }
}
