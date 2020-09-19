package de.ebuchner.vocab.config.preferences;

public class IntegerValue implements PreferenceValue {
    private Integer value;

    public IntegerValue() {

    }

    public IntegerValue(Integer value) {
        this.value = value;
    }

    public String asString() {
        if (value != null)
            return value.toString();
        return null;
    }

    public void fromString(String stringValue) {
        if (stringValue != null)
            this.value = Integer.parseInt(stringValue);
        else
            this.value = null;
    }

    public Integer getValue() {
        return value;
    }
}
