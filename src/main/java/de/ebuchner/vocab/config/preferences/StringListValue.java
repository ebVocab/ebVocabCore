package de.ebuchner.vocab.config.preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringListValue implements PreferenceValue {
    private List<String> strings = new ArrayList<String>();

    public static final String SEPARATOR = "\t";

    public StringListValue() {

    }

    public StringListValue(List<String> strings) {
        this.strings = strings;
    }

    public String asString() {
        if (strings != null) {
            StringBuilder builder = new StringBuilder();
            for (String string : strings) {
                if (builder.length() > 0)
                    builder.append(SEPARATOR);
                builder.append(string);
            }
            return builder.toString();
        }

        return "";
    }

    public void fromString(String stringValue) {
        this.strings = new ArrayList<String>();
        if (stringValue != null && stringValue.trim().length() > 0)
            this.strings.addAll(Arrays.asList(stringValue.split(SEPARATOR)));
    }

    public List<String> getStrings() {
        return strings;
    }
}
