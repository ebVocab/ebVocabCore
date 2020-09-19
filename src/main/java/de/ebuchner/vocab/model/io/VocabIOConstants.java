package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.config.fields.FieldFactory;

import java.util.Collections;
import java.util.List;

public interface VocabIOConstants {
    String VOCAB_PART_PREFIX = "vocab";
    String VOCAB_PART_SUFFIX = ".xml";

    String VOCAB_ROOT_NODE = "trainingUnit";
    String VOCAB_ENTRY_ELEMENT = "entry";
    String VOCAB_FIELD_ELEMENT = "field";
    String VOCAB_LABEL_ELEMENT = "label";
    String VOCAB_VALUE_ELEMENT = "value";
    String VOCAB_ATTRIBUTE_ID = "id";

    String VOCAB_REF_ROOT_NODE = "vocabRefList";
    String VOCAB_REF_ELEMENT = "vocabRef";
    String VOCAB_REF_FILE_ELEMENT = "fileRef";

    int VOCAB_IMAGE_MARGIN = 3;
    int VOCAB_IMAGE_BACKGROUND = 0x0;
    int VOCAB_IMAGE_FOREGROUND = 0xFFFFFF;
    String VOCAB_IMAGE_FORMAT = "png";
    String VOCAB_IMAGE_PATH_SUFFIX = "." + VOCAB_IMAGE_FORMAT;

    List<String> VOCAB_IMAGE_FIELDS = Collections.singletonList(FieldFactory.FOREIGN);

    String VOCAB_IMAGE_PATH_SEPARATOR = "/";
    String VOCAB_IMAGE_PATH_PREFIX = "alt/img";

    String VOCAB_META_PATH_PREFIX = "meta";
    String VOCAB_TIMESTAMP_FILE = "timestamp.txt";
}
