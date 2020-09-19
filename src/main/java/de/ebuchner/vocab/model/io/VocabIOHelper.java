package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.config.ConfigConstants;
import de.ebuchner.vocab.config.fields.Field;
import de.ebuchner.vocab.model.font.FontModel;
import de.ebuchner.vocab.model.font.VocabFont;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;
import de.ebuchner.vocab.model.nui.platform.UIPlatformFactory;
import de.ebuchner.vocab.model.nui.platform.UIPlatformType;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VocabIOHelper {

    private static final int MAX_CACHE_SIZE = 200;
    private static Map<File, VocabEntryList> entryCache = new HashMap<File, VocabEntryList>();
    private static Cache cache = Cache.UNKNOWN;

    private VocabIOHelper() {

    }

    public static VocabTimeStamp timeStampOf(File file) {
        try {
            VocabDecoder decoder = new VocabDecoder(new FileInputStream(file));
            try {
                return decoder.decodeTimeStamp();
            } finally {
                decoder.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // todo replace with fromFile()
    public static List<VocabEntryRef> fromRefFile(File file) throws IOException {
        RefDecoder decoder = null;
        try {
            decoder = new RefDecoder(new FileInputStream(file));
            return decoder.decode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (decoder != null)
                decoder.close();
        }

    }

    // todo buggy: Select ref-file in lesson, goto practice - edit entry - opens editor - but with wrong file name (of ref file - not vocab file)
    public static VocabEntryList fromFile(File file) {
        if (cache == Cache.UNKNOWN) {
            if (UIPlatformFactory.getUIPlatform().getType() == UIPlatformType.MOBILE)
                cache = Cache.ON;
            else
                cache = Cache.OFF;
        }

        if (cache == Cache.ON) {
            VocabEntryList entries = entryCache.get(file);
            if (entries != null)
                return entries;
        }

        try {
            IVocabDecoder decoder = VocabDecoderFactory.createVocabDecoder(file);
            try {
                VocabEntryList entries = decoder.decodeEntries();
                if (entries != null) {
                    if (cache == Cache.ON) {
                        if (entryCache.size() < MAX_CACHE_SIZE)
                            entryCache.put(file, entries);
                    }
                }
                return entries;
            } finally {
                decoder.close();
            }
        } catch (Exception e) {
            System.out.println("Error reading file " + file);
            throw new RuntimeException(e);
        }
    }

    public static VocabEntryList fromURL(URL url) {
        try {
            InputStream in = url.openStream();
            try {
                VocabDecoder decoder = new VocabDecoder(in);
                try {
                    return decoder.decodeEntries();
                } finally {
                    decoder.close();
                }
            } finally {
                in.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static VocabEntryList fromText(String text) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(
                    text.getBytes("utf-8")
            );
            return new FieldDecoder().decode(stream);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public static void toFile(File file, VocabEntryList entries) {
        try {
            VocabEncoder encoder = new VocabEncoder(new FileOutputStream(file));
            try {
                encoder.encode(entries);
            } finally {
                encoder.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void toRefFile(File file, List<VocabEntryRef> entryRefs) {
        RefEncoder encoder = new RefEncoder();
        try {
            FileOutputStream stream = new FileOutputStream(file);
            try {
                encoder.encode(stream, entryRefs);
            } finally {
                stream.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void toFileWithImage(File file, VocabEntryList entries) {
        try {
            VocabFont vocabFont = FontModel.getOrCreateFontModel().getImageFont();
            VocabEncoder encoder = new VocabEncoder(new FileOutputStream(file), vocabFont);
            try {
                encoder.encode(entries);
            } finally {
                encoder.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static byte[] imageFor(File fileRef, VocabEntry entry, Field field) {
        try {
            VocabDecoder decoder = new VocabDecoder(new FileInputStream(fileRef));
            return decoder.decodeBinary(entry.getId(), field.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String asString(File file) throws IOException {
        StringBuilder buffer = new StringBuilder();
        FileInputStream stream = new FileInputStream(file);
        try {
            int c;
            while ((c = stream.read()) != -1) {
                String hex = Integer.toHexString(c);
                if (hex.length() == 1)
                    buffer.append("0");
                buffer.append(hex);
            }
        } finally {
            stream.close();
        }
        return buffer.toString();
    }

    public static boolean isRefFile(File file) {
        return file.getName().endsWith(ConfigConstants.FILE_REF_EXTENSION);
    }

    private enum Cache {
        UNKNOWN, ON, OFF
    }
}
