package de.ebuchner.vocab.model.editor;

import de.ebuchner.vocab.model.lessons.entry.VocabEntry;

import java.io.File;
import java.util.List;

public interface EditorFileModelBehaviour {

    File getFile();

    void openFile(File file);

    boolean hasFile();

    void clearFile();

    boolean isDirty();

    boolean isEmpty();

    void saveFile(File file);

    void addEntry(VocabEntry entry);

    VocabEntry updateEntry(String id, VocabEntry entry);

    int entryCount();

    VocabEntry getEntry(int pos);

    void deleteEntries(List<VocabEntry> entries);

    void moveEntry(VocabEntry entry, EditorFileModel.MoveEntryDirection direction);

    int indexOf(VocabEntry entry);

    VocabEntry findEntry(String id);
}
