package de.ebuchner.vocab.model.io;

import java.util.StringTokenizer;

public class ImagePath implements VocabIOConstants {

    private String fieldLabel;
    private String entryId;

    public ImagePath(String entryId, String fieldLabel) {
        this.entryId = entryId;
        this.fieldLabel = fieldLabel;

        checkToken(entryId);
        checkToken(fieldLabel);
    }

    public ImagePath(String path) {
        if (!path.endsWith(VOCAB_IMAGE_PATH_SUFFIX))
            throw new IllegalArgumentException("path should end with " + VOCAB_IMAGE_PATH_SUFFIX);

        path = path.substring(0, path.length() - VOCAB_IMAGE_PATH_SUFFIX.length());

        if (!path.startsWith(VOCAB_IMAGE_PATH_PREFIX))
            throw new IllegalArgumentException("path should begin with " + VOCAB_IMAGE_PATH_PREFIX + ". Found " + path);
        path = path.substring(VOCAB_IMAGE_PATH_PREFIX.length());

        StringTokenizer tokenizer = new StringTokenizer(path, VOCAB_IMAGE_PATH_SEPARATOR);

        entryId = readToken(tokenizer);
        fieldLabel = readToken(tokenizer);
    }

    private String readToken(StringTokenizer tokenizer) {
        if (!tokenizer.hasMoreTokens())
            throw new IllegalArgumentException("Cannot parse image path");
        return tokenizer.nextToken();
    }

    public String toPath() {
        StringBuilder builder = new StringBuilder();
        builder.append(VOCAB_IMAGE_PATH_PREFIX);
        builder.append(VOCAB_IMAGE_PATH_SEPARATOR);
        builder.append(entryId);
        builder.append(VOCAB_IMAGE_PATH_SEPARATOR);
        builder.append(fieldLabel);
        builder.append(VOCAB_IMAGE_PATH_SUFFIX);

        return builder.toString();
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public String getEntryId() {
        return entryId;
    }

    private void checkToken(String token) {
        if (token == null)
            return;
        if (token.indexOf(VOCAB_IMAGE_PATH_SEPARATOR) >= 0)
            throw new IllegalArgumentException("Invalid name token " + token);
    }

}
