package de.ebuchner.vocab.model.search;

import de.ebuchner.toolbox.lang.Equals;
import de.ebuchner.toolbox.lang.HashCode;

import java.util.ArrayList;
import java.util.List;

public class VocabSearchOptions extends SearchOptions {

    private List<String> searchFields = new ArrayList<String>();

    public boolean isSearchField(String fieldName) {
        return searchFields.contains(fieldName);
    }

    public void addSearchField(String fieldName) {
        searchFields.add(fieldName);
    }

    public List<String> getSearchFields() {
        return searchFields;
    }

    @Override
    public boolean equals(Object o) {
        Equals equals = new Equals(this);
        return equals.compareWith(o);
    }

    @Override
    public int hashCode() {
        HashCode hashCode = new HashCode(this);
        hashCode.addBoolean(super.isCaseSensitive());
        hashCode.addBoolean(super.isRegularExpression());
        hashCode.addBoolean(super.isWholeWords());
        hashCode.addObjectArray(searchFields.toArray(new String[0]));

        return hashCode.getResult();
    }
}
