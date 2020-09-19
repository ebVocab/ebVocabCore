package de.ebuchner.vocab.model.nui.focus;

public interface FocusAware {

    void onFocusChangedTo(NuiTextFieldWithFocus textFieldWithFocus);

    void onFocusLost();

}
