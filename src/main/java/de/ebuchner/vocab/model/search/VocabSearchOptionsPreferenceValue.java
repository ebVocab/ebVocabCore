package de.ebuchner.vocab.model.search;

import de.ebuchner.vocab.config.preferences.PreferenceValue;
import de.ebuchner.vocab.config.preferences.StringListValue;

import java.util.ArrayList;
import java.util.List;

public class VocabSearchOptionsPreferenceValue implements PreferenceValue {
    private VocabSearchOptions searchOptions;

    public VocabSearchOptionsPreferenceValue() {
    }

    public VocabSearchOptionsPreferenceValue(VocabSearchOptions searchOptions) {
        this.searchOptions = searchOptions;
    }

    public String asString() {
        List<String> allValues = new ArrayList<String>();

        allValues.add(Boolean.toString(searchOptions.isCaseSensitive()));
        allValues.add(Boolean.toString(searchOptions.isRegularExpression()));
        allValues.add(Boolean.toString(searchOptions.isWholeWords()));

        for (String searchField : searchOptions.getSearchFields())
            allValues.add(searchField);

        StringListValue stringListValue = new StringListValue(allValues);
        return stringListValue.asString();
    }

    public void fromString(String stringValue) {
        StringListValue stringListValue = new StringListValue();
        stringListValue.fromString(stringValue);

        searchOptions = new VocabSearchOptions();
        List<String> allValues = stringListValue.getStrings();

        for (int index = 0; index < allValues.size(); index++) {
            String value = allValues.get(index);
            switch (index) {
                case 0:
                    searchOptions.setCaseSensitive(Boolean.parseBoolean(value));
                    break;
                case 1:
                    searchOptions.setRegularExpression(Boolean.parseBoolean(value));
                    break;
                case 2:
                    searchOptions.setWholeWords(Boolean.parseBoolean(value));
                    break;
                default:
                    searchOptions.addSearchField(value);
            }

        }
    }

    public VocabSearchOptions getSearchOptions() {
        return searchOptions;
    }
}
