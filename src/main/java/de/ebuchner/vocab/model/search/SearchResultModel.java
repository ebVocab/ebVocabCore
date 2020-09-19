package de.ebuchner.vocab.model.search;

import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.core.AbstractModel;
import de.ebuchner.vocab.model.core.ModelChangeEvent;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

import java.util.ArrayList;
import java.util.List;

public class SearchResultModel extends AbstractModel<SearchModelChange, SearchModelListener>
        implements SearchResultModelBehaviour {

    private static final String PREFERENCES_SEARCH_OPTIONS = "SEARCH_OPTIONS";
    private List<VocabEntryRef> entryRefList = new ArrayList<VocabEntryRef>();
    private List<VocabEntry> entryCache = new ArrayList<VocabEntry>();
    private VocabSearchOptions defaultSearchOptions;

    public static SearchResultModel getOrCreateSearchModel() {
        return (SearchResultModel) VocabModel.getInstance().getOrCreateModel(SearchResultModel.class);
    }

    public int getEntryCount() {
        return entryRefList.size();
    }

    public VocabEntryRef getEntryRef(int row) {
        return entryRefList.get(row);
    }

    public VocabEntry getEntry(int row) {
        return entryCache.get(row);
    }

    public void setResult(VocabSearcher.Result result) {
        entryRefList.clear();
        entryRefList.addAll(result.getEntryRefs());

        entryCache.clear();
        entryCache.addAll(result.getEntries());
    }

    public VocabSearchOptions getDefaultSearchOptions() {
        return defaultSearchOptions;
    }

    public void setDefaultSearchOptions(VocabSearchOptions defaultSearchOptions) {
        this.defaultSearchOptions = defaultSearchOptions;
    }

    @Override
    protected void fireModelChanged() {
        ModelChangeEvent event = new ModelChangeEvent(this);
        for (SearchModelListener listener : listeners) {
            listener.searchChanged(event);
        }
    }

    @Override
    public void reSynchronize() {

    }

    public void restoreFromPreferences(PreferenceValueList preferenceValues) {
        VocabSearchOptionsPreferenceValue preferenceValue =
                (VocabSearchOptionsPreferenceValue)
                        preferenceValues.getName(SearchResultModel.class, PREFERENCES_SEARCH_OPTIONS);
        if (preferenceValue != null)
            defaultSearchOptions = preferenceValue.getSearchOptions();
        else
            defaultSearchOptions = new VocabSearchOptions();
    }

    public void saveToPreferences(PreferenceValueList preferenceValues) {
        VocabSearchOptionsPreferenceValue preferenceValue =
                new VocabSearchOptionsPreferenceValue(defaultSearchOptions);

        preferenceValues.putName(
                SearchResultModel.class,
                PREFERENCES_SEARCH_OPTIONS,
                preferenceValue
        );
    }
}
