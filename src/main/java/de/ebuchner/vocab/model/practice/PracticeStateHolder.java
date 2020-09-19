package de.ebuchner.vocab.model.practice;

public interface PracticeStateHolder {

    void setPracticeState(PracticeState practiceState);

    PracticeState getPracticeState();

    void onPrepareAskNext();
}
