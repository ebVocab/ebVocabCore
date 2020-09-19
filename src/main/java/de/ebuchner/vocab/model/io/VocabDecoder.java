package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class VocabDecoder implements VocabIOConstants, IVocabDecoder {

    private ZipInputStream inputStream;

    public VocabDecoder(InputStream inputStream) {
        this.inputStream = new ZipInputStream(inputStream);
    }

    public void close() throws IOException {
        inputStream.close();
    }

    public VocabEntryList decodeEntries() {
        try {
            ZipEntry zipEntry;
            while ((zipEntry = inputStream.getNextEntry()) != null) {
                try {
                    String name = zipEntry.getName();
                    if (name.startsWith(VOCAB_PART_PREFIX) && name.endsWith(VOCAB_PART_SUFFIX))
                        return new FieldDecoder().decode(inputStream);
                } finally {
                    inputStream.closeEntry();
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public VocabTimeStamp decodeTimeStamp() {
        try {
            ZipEntry zipEntry;
            while ((zipEntry = inputStream.getNextEntry()) != null) {
                try {
                    String name = zipEntry.getName();
                    if (name.startsWith(VOCAB_META_PATH_PREFIX) && name.endsWith(VOCAB_TIMESTAMP_FILE)) {
                        byte b[] = new byte[1024];
                        int len = inputStream.read(b);
                        if (len > 0) {
                            String timestampString = new String(b, 0, len);
                            return new VocabTimeStamp(Long.parseLong(timestampString));
                        }
                    }

                } finally {
                    inputStream.closeEntry();
                }
            }
            return VocabTimeStamp.UNDEFINED;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] decodeBinary(String id, String fieldLabel) {
        ImagePath imagePath = new ImagePath(id, fieldLabel);

        try {
            ZipEntry zipEntry;
            while ((zipEntry = inputStream.getNextEntry()) != null) {
                try {
                    String name = zipEntry.getName();
                    if (name.startsWith(imagePath.toPath())) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int readLen;
                        while ((readLen = inputStream.read(buffer, 0, buffer.length)) > 0) {
                            outputStream.write(buffer, 0, readLen);
                        }
                        outputStream.flush();
                        outputStream.close();

                        return outputStream.toByteArray();
                    }
                } finally {
                    inputStream.closeEntry();
                }
            }
            return new byte[0];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
