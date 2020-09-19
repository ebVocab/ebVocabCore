package de.ebuchner.vocab.config.preferences;

import java.util.HashMap;
import java.util.Map;

public class StringMapValue implements PreferenceValue {

    private Map<String, String> stringMap = new HashMap<String, String>();

    public static final String KEY_VALUE_SEPARATOR = "\t";
    public static final String PAIR_SEPARATOR = "\f";

    public StringMapValue() {

    }

    public StringMapValue(Map<String, String> stringMap) {
        this.stringMap = stringMap;
    }

    public String asString() {
        if (stringMap != null) {
            StringBuilder builder = new StringBuilder();
            for (String key : stringMap.keySet()) {
                if (builder.length() > 0)
                    builder.append(PAIR_SEPARATOR);
                builder.append(key).append(KEY_VALUE_SEPARATOR).append(stringMap.get(key));
            }
            return builder.toString();
        }

        return "";
    }

    public void fromString(String stringValue) {
        this.stringMap = new HashMap<String, String>();
        if (stringValue == null || stringValue.trim().length() == 0)
            return;

        for (String pair : stringValue.split(PAIR_SEPARATOR)) {
            String[] keyValue = pair.split(KEY_VALUE_SEPARATOR);
            if (keyValue.length != 2)
                throw new IllegalArgumentException("Not a stringMap: " + stringValue + " " + keyValue.length);
            stringMap.put(keyValue[0], keyValue[1]);
        }
    }

    public Map<String, String> getStringMap() {
        return stringMap;
    }
}
