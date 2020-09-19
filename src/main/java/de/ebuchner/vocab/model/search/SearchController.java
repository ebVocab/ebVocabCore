package de.ebuchner.vocab.model.search;

import de.ebuchner.vocab.config.fields.FieldFactory;
import de.ebuchner.vocab.model.VocabBaseController;
import de.ebuchner.vocab.model.editor.EditorController;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

import java.util.Collections;
import java.util.List;

public class SearchController extends VocabBaseController {

    private SearchWindowBehaviour searchWindow;
    private SearchResultModelBehaviour searchResult;

    public SearchController(SearchWindowBehaviour searchWindow) {
        this.searchWindow = searchWindow;

        this.searchResult = searchWindow.decorateModel(SearchResultModel.getOrCreateSearchModel());
    }

    public void onSearchResult(VocabSearcher.Result result) {
        searchResult.setResult(result);
        searchWindow.sendMessageResultCount(result.size());
    }

    public VocabSearcher.Result onSearchStart(
            String searchString,
            boolean caseSensitive,
            boolean wholeWords,
            boolean regularExpression,
            boolean searchInComments) {
        VocabSearchOptions searchOptions = new VocabSearchOptions();
        searchOptions.setCaseSensitive(caseSensitive);
        searchOptions.setRegularExpression(regularExpression);
        searchOptions.setWholeWords(wholeWords);

        searchOptions.addSearchField(FieldFactory.USER);
        searchOptions.addSearchField(FieldFactory.FOREIGN);
        if (searchInComments)
            searchOptions.addSearchField(FieldFactory.COMMENT);

        if (searchString == null || searchString.trim().length() == 0) {
            searchWindow.sendMessageNoSearchString();
            return null;
        }

        searchResult.setDefaultSearchOptions(searchOptions);
        return new VocabSearcher(searchOptions).doSearch(searchString);
    }

    public void onWindowWasCreated() {
        onTableSelectionChanged(Collections.<VocabEntry>emptyList());

        VocabSearchOptions searchOptions = searchResult.getDefaultSearchOptions();
        searchWindow.setSearchOptions(
                searchOptions,
                searchOptions.isSearchField(FieldFactory.COMMENT)
        );
    }

    public void onTableSelectionChanged(List<VocabEntry> selectedEntries) {
        searchWindow.setResultElementsSelectedCount(selectedEntries.size());
    }

    public void onResultEdit(VocabEntryRef selectedEntryRef) {
        if (selectedEntryRef == null)
            return;

        EditorController.editVocabEntry(selectedEntryRef);
    }
}
