package de.ebuchner.vocab.model.search;

import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

public interface SearchResultModelBehaviour {
    int getEntryCount();

    VocabEntryRef getEntryRef(int row);

    VocabEntry getEntry(int row);

    void setResult(VocabSearcher.Result result);

    VocabSearchOptions getDefaultSearchOptions();

    void setDefaultSearchOptions(VocabSearchOptions vocabSearchOptions);
}
