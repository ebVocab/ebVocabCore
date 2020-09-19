package de.ebuchner.vocab.model.search;

import de.ebuchner.vocab.model.core.ModelChangeEvent;
import de.ebuchner.vocab.model.core.ModelListener;

public interface SearchModelListener extends ModelListener {
    void searchChanged(ModelChangeEvent event);
}

