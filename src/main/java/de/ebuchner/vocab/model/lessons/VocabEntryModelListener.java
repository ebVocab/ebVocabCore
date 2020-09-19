package de.ebuchner.vocab.model.lessons;

import de.ebuchner.vocab.model.core.ModelListener;

public interface VocabEntryModelListener extends ModelListener {
    void vocabEntryModelChanged(VocabEntryChangedEvent event);

    void repetitionModeActivated(RepetitionMode repetitionMode);
}
