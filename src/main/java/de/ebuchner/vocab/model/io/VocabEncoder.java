package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.model.font.VocabFont;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;
import de.ebuchner.vocab.model.nui.platform.UIPlatformFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class VocabEncoder implements VocabIOConstants {

    ZipOutputStream zipOutputStream;
    VocabFont font;

    public VocabEncoder(OutputStream outputStream) {
        this(outputStream, null);
    }

    public VocabEncoder(OutputStream outputStream, VocabFont font) {
        zipOutputStream = new ZipOutputStream(outputStream);
        this.font = font;
    }

    public void flush() {
        try {
            zipOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            zipOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void encode(VocabEntryList entries) {
        writeTimeStamp();

        new FieldEncoder().encodeZip(zipOutputStream, entries);
        if (font != null)
            UIPlatformFactory.getUIPlatform().newImageEncoder().encode(font, zipOutputStream, entries);
    }

    private void writeTimeStamp() {
        try {
            VocabTimeStamp vocabTimeStamp = new VocabTimeStamp();
            ZipEntry zipEntry = new ZipEntry(VOCAB_META_PATH_PREFIX + "/" + VOCAB_TIMESTAMP_FILE);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(Long.toString(vocabTimeStamp.getTimeStamp()).getBytes());
            zipOutputStream.closeEntry();
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

}
