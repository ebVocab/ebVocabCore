package de.ebuchner.vocab.model.lessons.entry;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VocabEntryList {
    private File fileRef;
    private List<VocabEntry> vocabEntries = new ArrayList<VocabEntry>();

    public Iterable<VocabEntry> entries() {
        return Collections.unmodifiableList(vocabEntries);
    }

    public void addEntry(VocabEntry vocabEntry) {
        if (vocabEntries.contains(vocabEntry))
            throw new IllegalArgumentException("Duplicate entry with ID " + vocabEntry.getId());
        vocabEntries.add(vocabEntry);
    }

    public int entryCount() {
        return vocabEntries.size();
    }

    public VocabEntry getEntry(int pos) {
        if (pos < 0 || pos >= vocabEntries.size())
            throw new IllegalArgumentException("Invalid pos " + pos + ". Number of entries: " + vocabEntries.size());
        return vocabEntries.get(pos);
    }

    public VocabEntry findEntry(VocabEntryRef entryRef) {
        // ignore fileRef - Id should be unique
        for (VocabEntry entry : vocabEntries) {
            if (entry.getId().equals(entryRef.getId()))
                return entry;
        }

        return null;
    }

    public File getFileRef() {
        return fileRef;
    }

    public void setFileRef(File fileRef) {
        this.fileRef = fileRef;
    }

    public VocabEntry findEntry(String id) {
        for (VocabEntry entry : vocabEntries) {
            if (entry.getId().equals(id))
                return entry;
        }

        return null;
    }

    public int indexOf(VocabEntry anEntry) {
        int index = 0;
        for (VocabEntry entry : vocabEntries) {
            if (entry.getId().equals(anEntry.getId()))
                return index;
            index++;
        }

        return -1;
    }

    public List<VocabEntry> toListOfEntries() {
        List<VocabEntry> list = new ArrayList<>();
        list.addAll(vocabEntries);
        return list;
    }
}
