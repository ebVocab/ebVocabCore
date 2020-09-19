package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.config.ConfigConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class VocabDecoderFactory {

    private VocabDecoderFactory() {

    }

    public static IVocabDecoder createVocabDecoder(File file) throws FileNotFoundException {
        if (file.getName().endsWith(ConfigConstants.FILE_EXTENSION))
            return new VocabDecoder(new FileInputStream(file));
        if (file.getName().endsWith(ConfigConstants.FILE_REF_EXTENSION))
            return new RefDecoder(new FileInputStream(file));
        throw new IllegalArgumentException(String.format("Unknown file extension in %s", file.getName()));
    }
}
