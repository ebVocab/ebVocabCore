package de.ebuchner.vocab.model.io;

public class VocabTimeStamp {

    private static final long UNDEFINED_VALUE = 0L;

    public final static VocabTimeStamp UNDEFINED = new VocabTimeStamp(UNDEFINED_VALUE);

    private long timeStamp;

    public VocabTimeStamp() {
        this(System.currentTimeMillis());
    }

    public VocabTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean isUndefined() {
        return timeStamp == UNDEFINED_VALUE;
    }
}
