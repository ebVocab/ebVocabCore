package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.model.font.VocabFont;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;

import java.util.zip.ZipOutputStream;

public interface ImageEncoderBehaviour {

    void encode(VocabFont font, ZipOutputStream outputStream, VocabEntryList entries);
}
