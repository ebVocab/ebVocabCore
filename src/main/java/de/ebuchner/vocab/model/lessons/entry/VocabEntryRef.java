package de.ebuchner.vocab.model.lessons.entry;

import de.ebuchner.vocab.model.lessons.Lesson;

import java.io.File;

public class VocabEntryRef {
    private File fileRef;
    private String id;
    private String fileRefString;

    public VocabEntryRef(File fileRef, String id) {
        this.fileRef = fileRef;
        this.id = id;
    }

    public static VocabEntryRef fromEntry(Lesson lesson, VocabEntry entry) {
        File fileRef = null;
        if (lesson != null)
            fileRef = lesson.getFileRef();
        return new VocabEntryRef(fileRef, entry.getId());
    }

    public static VocabEntryRef fromEntry(File fileRef, VocabEntry entry) {
        return new VocabEntryRef(fileRef, entry.getId());
    }

    public String getId() {
        return id;
    }

    public File getFileRef() {
        return fileRef;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof VocabEntryRef))
            return false;
        return id.equals(((VocabEntryRef) o).getId());
    }

    @Override
    public String toString() {
        return "VocabEntryRef ID = " + id + ", file = " + fileRef;
    }

    public String getFileRefString() {
        return fileRefString;
    }

    public void setFileRefString(String fileRefString) {
        this.fileRefString = fileRefString;
    }
}
