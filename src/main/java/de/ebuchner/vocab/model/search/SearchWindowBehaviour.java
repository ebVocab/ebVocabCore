package de.ebuchner.vocab.model.search;

public interface SearchWindowBehaviour {
    SearchResultModelBehaviour decorateModel(SearchResultModelBehaviour searchResultModel);

    void sendMessageNoSearchString();

    void setResultElementsSelectedCount(int selectedCount);

    void sendMessageResultCount(int resultCount);

    void setSearchOptions(SearchOptions searchOptions, boolean searchInComments);
}
