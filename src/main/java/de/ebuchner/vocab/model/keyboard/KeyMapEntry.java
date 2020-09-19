package de.ebuchner.vocab.model.keyboard;

import java.util.ArrayList;
import java.util.List;

public class KeyMapEntry {

    private String locale;
    private int row;
    private int column;
    private boolean capitalOnly;
    private List<KeyMode> keyModes = new ArrayList<KeyMode>();

    public KeyMapEntry(String locale) {
        this.locale = locale;
    }

    public String generatedTextForKeyModifier(KeyModifier keyModifier) {
        String generatedText = null;
        for (KeyMode keyMode : keyModes) {
            if (keyMode.getModifier().equals(keyModifier))
                generatedText = keyMode.getGenerateText();
        }
        return generatedText;
    }

    public String getLocale() {
        return locale;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isCapitalOnly() {
        return capitalOnly;
    }

    public void setCapitalOnly(boolean capitalOnly) {
        this.capitalOnly = capitalOnly;
    }

    public List<KeyMode> getKeyModes() {
        return keyModes;
    }
}
