package de.ebuchner.vocab.model.lessons;

public class VocabEntryChangedEvent {

    private ChangeType changeType;

    public VocabEntryChangedEvent(ChangeType changeType) {
        this.changeType = changeType;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public static enum ChangeType {
        ACTIVE_SET, INACTIVE_SET
    }
}
