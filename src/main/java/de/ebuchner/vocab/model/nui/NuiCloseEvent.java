package de.ebuchner.vocab.model.nui;

public class NuiCloseEvent {

    private NuiWindow source;
    private CloseType closeType;
    public NuiCloseEvent(NuiWindow source, CloseType closeType) {
        this.source = source;
        this.closeType = closeType;
    }

    public NuiWindow getSource() {
        return source;
    }

    public CloseType getCloseType() {
        return closeType;
    }

    public static enum CloseType {
        OK, CANCEL, CLOSED
    }
}
