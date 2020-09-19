package de.ebuchner.vocab.config.fields;

public class GenericField implements Field {

    private String name;

    private boolean optional;
    private boolean multiLine;
    private boolean editable;
    private boolean active;

    private boolean practiceHidden;
    private boolean practiceReverseHidden;
    private boolean internal;

    public GenericField(
            String name,
            boolean editable,
            boolean optional,
            boolean multiLine,
            boolean active,
            boolean practiceHidden,
            boolean practiceReverseHidden,
            boolean internal
    ) {
        this.name = name;
        this.editable = editable;
        this.optional = optional;
        this.multiLine = multiLine;
        this.active = active;
        this.practiceHidden = practiceHidden;
        this.practiceReverseHidden = practiceReverseHidden;
        this.internal = internal;
    }

    public boolean editable() {
        return editable;
    }

    public boolean multiLine() {
        return multiLine;
    }

    public boolean optional() {
        return optional;
    }

    public boolean active() {
        return active;
    }

    public boolean practiceHidden() {
        return practiceHidden;
    }

    public boolean practiceReverseHidden() {
        return practiceReverseHidden;
    }

    public String name() {
        return name;
    }

    public boolean internal() {
        return internal;
    }

}
