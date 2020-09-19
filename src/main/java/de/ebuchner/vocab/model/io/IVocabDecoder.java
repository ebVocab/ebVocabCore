package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;

import java.io.IOException;

public interface IVocabDecoder {
    VocabEntryList decodeEntries();

    void close() throws IOException;
}
