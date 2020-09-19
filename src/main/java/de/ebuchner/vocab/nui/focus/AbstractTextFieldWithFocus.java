package de.ebuchner.vocab.nui.focus;

import de.ebuchner.vocab.model.nui.focus.NuiTextFieldWithFocus;

public abstract class AbstractTextFieldWithFocus implements NuiTextFieldWithFocus {

    public abstract int getCaretPosition();

    public abstract void setCaretPosition(int caretPosition);

    public abstract int getSelectionStart();

    public abstract int getSelectionEnd();

    public final void addText(String input) {
        if (!isEditable())
            return;

        int pos = getCaretPosition();
        String text = getText();
        if (text == null)
            text = "";

        int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        if (selEnd == selStart)
            selEnd = selStart = pos;

        changeText(text.substring(0, selStart) + input + text.substring(selEnd));
        setCaretPosition(selStart + input.length());
    }

    public final void onKeyBackspace() {
        if (!isEditable())
            return;

        int pos = getCaretPosition();
        String text = getText();

        int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        if (selEnd == selStart) {
            selStart = pos - 1;
            if (selStart >= text.length())
                selStart = text.length() - 1;
            if (selStart < 0)
                return;
            selEnd = selStart + 1;

        }

        String newText = text.substring(0, selStart) + text.substring(selEnd);
        changeText(newText);

        setCaretPosition(selStart);
    }

    protected abstract void changeText(String text);

}
