package de.ebuchner.vocab.config.preferences;

public class StringValue implements PreferenceValue {
    protected String string;

    public StringValue() {

    }

    public StringValue(String string) {
        this.string = string;
    }

    public String asString() {
        return string;
    }

    public void fromString(String stringValue) {
        this.string = stringValue;
    }

    public String getString() {
        return string;
    }
}
