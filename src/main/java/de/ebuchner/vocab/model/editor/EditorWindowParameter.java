package de.ebuchner.vocab.model.editor;

import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;
import de.ebuchner.vocab.model.nui.NuiWindowParameter;

public class EditorWindowParameter implements NuiWindowParameter {
    private final VocabEntryRef vocabEntryRef;
    private final Action action;
    public EditorWindowParameter(Action action) {
        this(action, null);
    }

    public EditorWindowParameter(Action action, VocabEntryRef vocabEntryRef) {
        this.action = action;
        this.vocabEntryRef = vocabEntryRef;
    }

    public VocabEntryRef getVocabEntryRef() {
        return vocabEntryRef;
    }

    public Action getAction() {
        return action;
    }

    public String getToken() {
        return action.name().toLowerCase();
    }

    public static enum Action {
        OPEN, NEW
    }
}
