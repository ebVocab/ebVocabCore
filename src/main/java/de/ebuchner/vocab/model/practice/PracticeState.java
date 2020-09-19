package de.ebuchner.vocab.model.practice;

public abstract class PracticeState {
    protected PracticeStateHolder stateHolder;

    public PracticeState(PracticeStateHolder stateHolder) {
        this.stateHolder = stateHolder;
    }

    public abstract boolean stateAskNextAvailable();

    public abstract void stateNext();

    public abstract void stateDisable();

    public abstract void stateAskNext();

    public abstract void updateWindow(PracticeModel practiceModel, PracticeWindowBehaviour practiceWindow);

    public abstract void fireFocusEvent(PracticeWindowBehaviour practiceWindow);

    public abstract void forceAskNext();

    public abstract boolean isDisabled();
}
