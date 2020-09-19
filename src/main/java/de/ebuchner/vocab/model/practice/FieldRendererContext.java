package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.fields.Field;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;

import java.io.File;

public class FieldRendererContext {

    private File fileRef;
    private Field field;
    private String value;
    private boolean fieldHidden;
    private boolean valueNotEmpty;
    private VocabEntry currentEntry;

    public FieldRendererContext(
            File fileRef,
            Field field,
            String value,
            boolean fieldHidden,
            boolean valueNotEmpty,
            VocabEntry currentEntry
    ) {
        this.fileRef = fileRef;
        this.field = field;
        this.value = value;
        this.fieldHidden = fieldHidden;
        this.valueNotEmpty = valueNotEmpty;
        this.currentEntry = currentEntry;
    }

    public Field getField() {
        return field;
    }

    public boolean isValueNotEmpty() {
        return valueNotEmpty;
    }

    public boolean isFieldHidden() {
        return fieldHidden;
    }

    public File getFileRef() {
        return fileRef;
    }

    public String getValue() {
        return value;
    }

    public VocabEntry getCurrentEntry() {
        return currentEntry;
    }
}
