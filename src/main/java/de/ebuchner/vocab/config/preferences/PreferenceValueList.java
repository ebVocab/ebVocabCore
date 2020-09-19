package de.ebuchner.vocab.config.preferences;

import java.util.HashMap;
import java.util.Map;

public class PreferenceValueList {

    private Map<String, PreferenceValue> entries = new HashMap<String, PreferenceValue>();

    void clear() {
        entries.clear();
    }

    Iterable<String> names() {
        return entries.keySet();
    }

    public void putName(String name, PreferenceValue value) {
        entries.put(name, value);
    }

    public void putName(Class clazz, String name, PreferenceValue value) {
        entries.put(nameOf(clazz, name), value);
    }

    public void removeName(String name) {
        if (entries.containsKey(name))
            entries.remove(name);
    }

    public PreferenceValue getName(String name) {
        return entries.get(name);
    }

    public PreferenceValue getName(Class clazz, String name) {
        return getName(nameOf(clazz, name));
    }

    private String nameOf(Class clazz, String name) {
        return clazz.getName() + "_" + name;
    }

    public void removeName(Class clazz, String name) {
        removeName(nameOf(clazz, name));
    }
}
