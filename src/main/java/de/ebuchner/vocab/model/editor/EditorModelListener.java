package de.ebuchner.vocab.model.editor;

import de.ebuchner.vocab.model.core.ModelChangeEvent;
import de.ebuchner.vocab.model.core.ModelListener;

public interface EditorModelListener extends ModelListener {
    void editorChanged(ModelChangeEvent event);
}
