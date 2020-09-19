package de.ebuchner.vocab.model.nui;

import de.ebuchner.vocab.model.nui.focus.FocusAware;

public interface NuiEventListener extends FocusAware {

    NuiClosingResult onNuiWindowClosing(NuiCloseEvent event);

    void onNuiWindowClosed(NuiCloseEvent event);

}
