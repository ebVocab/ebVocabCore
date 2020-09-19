package de.ebuchner.vocab.model.practice;

import java.io.File;

public interface RepetitionLoadActivityBehaviour {
    void sendRepetitionToCallerAndExit(File repetitionFile);

    void sendCancelToCallerAndExit();

    void onModelChanged(RepetitionLoadModel model);

    void selectedRepetitionDeleted();
}
