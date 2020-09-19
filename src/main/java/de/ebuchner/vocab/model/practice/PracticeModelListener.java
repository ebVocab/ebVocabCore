package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.model.core.ModelChangeEvent;
import de.ebuchner.vocab.model.core.ModelListener;

public interface PracticeModelListener extends ModelListener {
    void practiceChanged(ModelChangeEvent event);
}
