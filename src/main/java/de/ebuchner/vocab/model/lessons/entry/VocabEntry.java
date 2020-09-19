package de.ebuchner.vocab.model.lessons.entry;

import de.ebuchner.vocab.tools.RandomTools;

import java.util.*;

public class VocabEntry {
    private String id;
    private Map<String, String> fields = new HashMap<String, String>();

    public VocabEntry() {
        this(RandomTools.nextId());
    }

    public VocabEntry(String id) {
        this.id = id;
    }

    public static VocabEntry copyOf(VocabEntry entry) {
        VocabEntry copy = new VocabEntry(); // new id
        for (String key : entry.fields.keySet()) {
            copy.fields.put(key, entry.fields.get(key));
        }
        return copy;
    }

    public void calculateAnotherId() {
        this.id = RandomTools.nextId();
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
        /*
        HashCode hc = new HashCode(this);
        hc.addObject(id);
        return hc.getResult();
        */
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || (!(o instanceof VocabEntry)))
            return false;
        return id.equals(((VocabEntry) o).getId());
        //return new Equals(this).compareWith(o);
    }

    public Iterable<String> fieldNames() {
        return Collections.unmodifiableSet(fields.keySet());
    }

    public String getFieldValue(String fieldName) {
        return fields.get(fieldName);
    }

    public void putFieldValue(String fieldName, String fieldValue) {
        fields.put(fieldName, fieldValue);
    }

    public int fieldCount() {
        return fields.size();
    }

    public boolean updateFieldsFrom(VocabEntry other) {
        boolean changed = false;

        List<String> myFieldNames = new ArrayList<String>();
        myFieldNames.addAll(fields.keySet());

        for (String fieldName : myFieldNames) {
            if (updateFieldsFrom(fieldName, getFieldValue(fieldName), other.getFieldValue(fieldName)))
                changed = true;
        }

        List<String> otherFieldNames = new ArrayList<String>();
        otherFieldNames.addAll(other.fields.keySet());

        for (String fieldName : otherFieldNames) {
            if (updateFieldsFrom(fieldName, getFieldValue(fieldName), other.getFieldValue(fieldName)))
                changed = true;
        }

        return changed;
    }

    private boolean updateFieldsFrom(String fieldName, String myValue, String otherValue) {
        if (myValue == null && otherValue == null)
            return false;
        else if (myValue != null && otherValue != null) {
            if (myValue.equals(otherValue))
                return false;
            fields.put(fieldName, otherValue);
            return true;
        } else {
            if (myValue == null) {
                fields.put(fieldName, otherValue);
                return true;
            } else {
                fields.remove(fieldName);
                return true;
            }
        }
    }
}
