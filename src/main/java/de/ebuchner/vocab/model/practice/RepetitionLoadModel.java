package de.ebuchner.vocab.model.practice;

import java.io.File;

public class RepetitionLoadModel {
    public static final String REPETITION_FILE = "repetition.file";
    private File repetitionFile;
    private RepetitionLoadModelListener modelListener;

    public File getRepetitionFile() {
        return repetitionFile;
    }

    public void setRepetitionFile(File repetitionFile) {
        this.repetitionFile = repetitionFile;
        fireModelChanged();
    }

    private void fireModelChanged() {
        if (modelListener != null)
            modelListener.onModelChanged(this);
    }

    public void setModelListener(RepetitionLoadModelListener modelListener) {
        this.modelListener = modelListener;
    }

    public boolean hasRepetitionFile() {
        return repetitionFile != null;
    }

    public void clearRepetitionFile() {
        this.repetitionFile = null;
        fireModelChanged();
    }
}
