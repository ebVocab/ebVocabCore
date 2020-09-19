package de.ebuchner.vocab.model.search;

public class SearchOptions {

    private boolean caseSensitive;
    private boolean wholeWords;
    private boolean regularExpression;

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isWholeWords() {
        return wholeWords;
    }

    public void setWholeWords(boolean wholeWords) {
        this.wholeWords = wholeWords;
    }

    public boolean isRegularExpression() {
        return regularExpression;
    }

    public void setRegularExpression(boolean regularExpression) {
        this.regularExpression = regularExpression;
    }
}
