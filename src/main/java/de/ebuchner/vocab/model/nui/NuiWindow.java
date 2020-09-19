package de.ebuchner.vocab.model.nui;

public interface NuiWindow {

    WindowType windowType();

    void nuiWindowCreate();

    void nuiWindowShow(NuiWindowParameter parameter);

    void addEventListener(NuiEventListener eventListener);

    void removeEventListener(NuiEventListener eventListener);

    WindowTypeFilter windowTypeFilter();

    boolean attemptClosing();

    void nuiWindowReceive(NuiWindowParameter windowParameter);

    boolean canCreate(NuiWindowParameter windowParameter);

    /**
     * called after nuiWindowCreate and the new window is registered. Necessary to handle
     * the following situation:
     * if practice window detects that no lessons are selected it opens the lesson window.
     * If lesson window is closed again it is then the last window and therefore Vocab exits
     * VOCAB-104
     */
    void onWindowInstanceRegistered();
}
