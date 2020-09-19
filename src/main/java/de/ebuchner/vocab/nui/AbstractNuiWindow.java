package de.ebuchner.vocab.nui;

import de.ebuchner.vocab.model.nui.*;
import de.ebuchner.vocab.model.nui.focus.NuiTextFieldWithFocus;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNuiWindow implements NuiWindow {

    protected List<NuiEventListener> listeners = new ArrayList<NuiEventListener>();

    public final void addEventListener(NuiEventListener eventListener) {
        listeners.add(eventListener);
    }

    public final void removeEventListener(NuiEventListener eventListener) {
        listeners.remove(eventListener);
    }

    protected final NuiClosingResult fireOnNuiWindowClosing(NuiCloseEvent.CloseType closeType) {
        NuiCloseEvent event = new NuiCloseEvent(this, closeType);
        for (NuiEventListener listener : listeners) {
            if (NuiClosingResult.CLOSING_NOT_ALLOWED == listener.onNuiWindowClosing(event))
                return NuiClosingResult.CLOSING_NOT_ALLOWED;
        }
        return NuiClosingResult.CLOSING_OK;
    }

    protected final void fireOnNuiWindowClosed(NuiCloseEvent.CloseType closeType) {
        NuiCloseEvent event = new NuiCloseEvent(this, closeType);

        // allow to remove listeners during event handling
        List<NuiEventListener> localListeners = new ArrayList<NuiEventListener>();
        localListeners.addAll(listeners);

        for (NuiEventListener listener : localListeners) {
            listener.onNuiWindowClosed(event);
        }
    }

    protected final void fireOnFocusChangedTo(NuiTextFieldWithFocus textFieldWithFocus) {
        for (NuiEventListener listener : listeners) {
            listener.onFocusChangedTo(textFieldWithFocus);
        }
    }

    protected final void fireOnFocusChangeToNonEditableControl() {
        for (NuiEventListener listener : listeners) {
            listener.onFocusLost();
        }
    }

    public WindowTypeFilter windowTypeFilter() {
        return new DefaultWindowTypeFilter(windowType());
    }

    public void nuiWindowReceive(NuiWindowParameter windowParameter) {

    }

    public boolean canCreate(NuiWindowParameter windowParameter) {
        return true;
    }

    public void onWindowInstanceRegistered() {

    }
}
