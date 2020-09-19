package de.ebuchner.vocab.config.preferences;

public class BooleanValue implements PreferenceValue {
    private Boolean value;

    public BooleanValue() {

    }

    public BooleanValue(boolean value) {
        this.value = value;
    }

    public String asString() {
        if (value != null)
            return value.toString();
        return null;
    }

    public void fromString(String stringValue) {
        if (stringValue != null)
            this.value = Boolean.parseBoolean(stringValue);
        else
            this.value = null;
    }

    public boolean getValue() {
        return value;
    }
}
