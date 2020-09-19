package de.ebuchner.vocab.model.nui.focus;

public interface NuiTextFieldWithFocus {

    String getText();

    void addText(String text);

    boolean isEditable();

    void onKeyBackspace();
}
