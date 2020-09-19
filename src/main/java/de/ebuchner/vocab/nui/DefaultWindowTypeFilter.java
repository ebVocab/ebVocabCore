package de.ebuchner.vocab.nui;

import de.ebuchner.vocab.model.nui.WindowType;
import de.ebuchner.vocab.model.nui.WindowTypeFilter;

public class DefaultWindowTypeFilter implements WindowTypeFilter {
    private WindowType windowType;

    public DefaultWindowTypeFilter(WindowType windowType) {
        this.windowType = windowType;
    }

    public boolean accept(WindowType otherWindowType) {
        if (otherWindowType.equals(WindowType.KEYBOARD_WINDOW))
            return windowType.getExtraKeyboard() == WindowType.ExtraKeyboard.ENABLED;

        if (windowType.equals(otherWindowType)) {
            if (windowType.getMaxInstances() == WindowType.MaxInstances.SINGLE)
                return false;
            // Editor toolbar already has new/open/save menu buttons
            if (windowType == WindowType.EDITOR_WINDOW)
                return false;
            return true;
        }

        if (otherWindowType == WindowType.TRANSLITERATION_WINDOW)
            return windowType == WindowType.EDITOR_WINDOW;

        if (otherWindowType == WindowType.LESSONS_WINDOW)
            return windowType == WindowType.PRACTICE_WINDOW;

        return true;
    }
}
