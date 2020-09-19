package de.ebuchner.vocab.model.editor;

import de.ebuchner.vocab.model.lessons.entry.VocabEntry;

public interface EntrySelectionBehaviour {

    int selectionCount();

    int allEntriesCount();

    Iterable<Integer> selectedModelEntryRows();

    void clearSelection();

    void addSelection(VocabEntry entry);
}
