package de.ebuchner.vocab.model.editor;

import de.ebuchner.vocab.model.lessons.entry.VocabEntry;

public interface EntryInEditWindowBehaviour {
    VocabEntry entryFromUI();

    void sendMessageFieldMissing();

    void entryToUI(VocabEntry entry);
}
