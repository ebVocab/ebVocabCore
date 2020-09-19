package de.ebuchner.vocab.model.search;

import java.util.regex.Pattern;

public class TextSearcher {

    private Pattern searchPattern;

    public TextSearcher(SearchOptions searchOptions, String searchExpression) {
        if (searchExpression == null)
            return;
        searchExpression = searchExpression.trim();

        if (!searchOptions.isRegularExpression())
            searchExpression = Pattern.quote(searchExpression);

        if (searchOptions.isWholeWords())
            searchExpression = "\\b" + searchExpression + "\\b";

        if (!searchOptions.isCaseSensitive())
            this.searchPattern = Pattern.compile(
                    searchExpression,
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
            );
        else
            this.searchPattern = Pattern.compile(searchExpression);

    }


    public boolean matches(String text) {
        if (searchPattern == null)
            return false;
        if (text == null || text.trim().length() == 0)
            return false;

        return searchPattern.matcher(text.trim()).find();
    }
}
