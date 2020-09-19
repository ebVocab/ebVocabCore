package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.fields.Field;

public class PracticeStateDisabled extends PracticeState {
    public PracticeStateDisabled(PracticeStateHolder stateHolder) {
        super(stateHolder);
    }

    @Override
    public void stateNext() {
        stateAskNext();
    }

    @Override
    public void forceAskNext() {
        // ignore
    }

    @Override
    public boolean isDisabled() {
        return true;
    }

    @Override
    public void stateDisable() {

    }

    @Override
    public void stateAskNext() {
        stateHolder.setPracticeState(new PracticeStateAskNext(stateHolder));
    }

    @Override
    public void updateWindow(PracticeModel practiceModel, PracticeWindowBehaviour practiceWindow) {
        practiceModel.clearCurrentFile();

        practiceWindow.setStrategyOptionsEnabled(false);

        practiceWindow.setNavigationForwardEnabled(false);
        practiceWindow.setNavigationBackwardEnabled(false);
        practiceWindow.setNavigationReshuffleEnabled(false);
        practiceWindow.setNavigationResetEnabled(false);

        for (Field field : Config.instance().fieldListVisible())
            practiceWindow.setFieldEnabled(field, false);
        fireFocusEvent(practiceWindow);

        practiceWindow.setLessonFieldsEnabled(false);

        practiceWindow.setReverseOptionEnabled(false);
        practiceWindow.setImageOptionEnabled(false);
        practiceWindow.setShowStatisticsOptionEnabled(false);

        // repetition
        practiceWindow.setRepetitionClearEnabled(false);
        practiceWindow.setRepetitionClearVisible(false);
        practiceWindow.setRepetitionModeEnabled(false);
        practiceWindow.setRepetitionAddEnabled(false);
        practiceWindow.setRepetitionAddVisible(false);
        practiceWindow.setRepetitionRemoveEnabled(false);
        practiceWindow.setRepetitionRemoveVisible(false);
        practiceWindow.setRepetitionCount(0);
        practiceWindow.setPracticeProgress(PracticeProgress.EMPTY);

    }

    @Override
    public void fireFocusEvent(PracticeWindowBehaviour practiceWindow) {
        practiceWindow.firePracticeWindowOnUnFocus();
    }

    @Override
    public boolean stateAskNextAvailable() {
        return false;
    }
}
