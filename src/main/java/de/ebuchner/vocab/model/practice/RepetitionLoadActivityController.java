package de.ebuchner.vocab.model.practice;

import java.io.File;
import java.util.logging.Logger;

public class RepetitionLoadActivityController implements RepetitionLoadModelListener {

    private static final Logger logger = Logger.getLogger(RepetitionLoadActivityController.class.getName());
    private final RepetitionLoadActivityBehaviour activity;
    private final RepetitionLoadModel model;

    public RepetitionLoadActivityController(RepetitionLoadModel model, RepetitionLoadActivityBehaviour activity) {
        this.model = model;
        model.setModelListener(this);

        this.activity = activity;
    }

    public void onOk() {
        activity.sendRepetitionToCallerAndExit(
                model.getRepetitionFile()
        );
    }

    public void onCancel() {
        activity.sendCancelToCallerAndExit();
    }

    public void onRepetitionSelected(File repetitionFile) {
        model.setRepetitionFile(repetitionFile);
    }

    @Override
    public void onModelChanged(RepetitionLoadModel repetitionLoadModel) {
        activity.onModelChanged(model);
    }

    public void onDeleteSelected() {
        if (!model.hasRepetitionFile())
            return;
        File repetitionFile = model.getRepetitionFile();
        if (!repetitionFile.delete())
            logger.severe(String.format("Could not delete %s", repetitionFile));

        activity.selectedRepetitionDeleted();
        model.clearRepetitionFile();
    }

    public void onRepetitionSelectionCleared() {
        model.clearRepetitionFile();
    }
}
