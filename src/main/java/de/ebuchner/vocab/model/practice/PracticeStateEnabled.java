package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.fields.Field;
import de.ebuchner.vocab.model.lessons.LessonModel;
import de.ebuchner.vocab.model.lessons.RepetitionMode;
import de.ebuchner.vocab.model.lessons.VocabEntryModel;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

import java.io.File;

public abstract class PracticeStateEnabled extends PracticeState {

    public PracticeStateEnabled(PracticeStateHolder stateHolder) {
        super(stateHolder);
    }

    @Override
    public void stateDisable() {
        stateHolder.setPracticeState(new PracticeStateDisabled(stateHolder));
    }

    @Override
    public void updateWindow(PracticeModel practiceModel, PracticeWindowBehaviour practiceWindow) {
        // strategy
        SelectedStrategy selectedStrategy = practiceModel.getSelectedStrategy();
        AbstractPracticeStrategy strategy = AbstractPracticeStrategy.getOrCreateStrategyModel(selectedStrategy);
        practiceWindow.setStrategyOptionsEnabled(true);
        practiceWindow.setSelectedStrategy(selectedStrategy);

        // navigation
        practiceWindow.setNavigationForwardEnabled(true);
        practiceWindow.setNavigationBackwardEnabled(strategy.canUndo());
        boolean reshuffledEnabled = true;
        if (strategy.supportsGotoPositions()) {
            reshuffledEnabled = strategy.getPracticeSize() > 1;
        }
        practiceWindow.setNavigationReshuffleEnabled(reshuffledEnabled);
        practiceWindow.setNavigationResetEnabled(strategy.getPracticePosition() > 0);

        // fields
        practiceWindow.setLessonFieldsEnabled(true);

        for (Field field : Config.instance().fieldListVisible()) {
            practiceWindow.setFieldEnabled(field, true);
        }

        // reverse
        practiceWindow.setReverseOptionEnabled(true);
        practiceWindow.setReverseOption(practiceModel.getReverse());

        // image
        practiceWindow.setImageOptionEnabled(true);
        practiceWindow.setImageOption(practiceModel.getImageOption());

        // show statistics
        practiceWindow.setShowStatisticsOptionEnabled(true);
        practiceWindow.setShowStatisticsOption(practiceModel.isShowStatistics());

        // fields
        VocabEntryRef currentRef = strategy.currentRef();
        VocabEntry currentEntry = strategy.currentEntry();
        if (currentEntry == null) {
            System.out.println("Current entry null for " + currentRef);
            stateDisable();
            return;
        }

        practiceModel.setCurrentRef(currentRef);
        updateFields(practiceModel, practiceWindow, currentRef.getFileRef(), currentEntry);

        // lesson field
        if (letStateDecideIfLessonIsShown(practiceModel))
            practiceWindow.setLessonFieldFileRef(currentRef.getFileRef());
        else
            practiceWindow.setLessonFieldFileRef(null);

        practiceWindow.setLessonFieldShow(practiceModel.isShowLessonFile());

        // repetition
        VocabEntryModel vocabEntryModel = VocabEntryModel.getOrCreateVocabEntryModel();
        RepetitionMode repetitionMode = vocabEntryModel.getRepetitionMode();
        boolean currentRefInRepetition = vocabEntryModel.isInRepetition(currentRef);
        boolean hasRepetitions = vocabEntryModel.hasRepetitions();

        practiceWindow.setRepetitionClearEnabled(hasRepetitions);
        practiceWindow.setRepetitionClearVisible(hasRepetitions);
        practiceWindow.setRepetitionMode(repetitionMode);
        practiceWindow.setRepetitionCount(vocabEntryModel.repetitionCount());
        practiceWindow.setRepetitionModeEnabled(hasRepetitions);

        if (repetitionMode == RepetitionMode.OFF) {
            practiceWindow.setRepetitionAddEnabled(!currentRefInRepetition);
            practiceWindow.setRepetitionAddVisible(!currentRefInRepetition);
            practiceWindow.setRepetitionRemoveEnabled(currentRefInRepetition);
            practiceWindow.setRepetitionRemoveVisible(currentRefInRepetition);
        } else {
            practiceWindow.setRepetitionAddEnabled(false);
            practiceWindow.setRepetitionAddVisible(false);
            practiceWindow.setRepetitionRemoveEnabled(true);
            practiceWindow.setRepetitionRemoveVisible(true);
        }

        boolean showStatistics = practiceModel.isShowStatistics();

        int usageCount = 0;
        long lastUsage = 0;

        PracticeStatisticsValue statistics = statisticsValueForDisplay();
        if (statistics == null || statistics.getUsageCount() == 0)
            showStatistics = false;
        else {
            usageCount = statistics.getUsageCount();
            lastUsage = statistics.getTimestamp();
        }

        practiceWindow.setPracticeProgress(
                new PracticeProgress(
                        LessonModel.getOrCreateLessonModel().countSelectedLessons(),
                        strategy.getPracticeSize(),
                        strategy.getPracticePosition(),
                        repetitionMode == RepetitionMode.ON,
                        usageCount,
                        lastUsage,
                        showStatistics
                )
        );

        fireFocusEvent(practiceWindow);
    }

    protected abstract PracticeStatisticsValue statisticsValueForDisplay();

    protected abstract boolean letStateDecideIfLessonIsShown(PracticeModel practiceModel);

    protected abstract void updateFields(
            PracticeModel practiceModel,
            PracticeWindowBehaviour practiceWindow,
            File fileRef,
            VocabEntry currentEntry
    );

    @Override
    public void fireFocusEvent(PracticeWindowBehaviour practiceWindow) {
        practiceWindow.firePracticeWindowOnFocusChanged();
    }

    @Override
    public boolean isDisabled() {
        return false;
    }
}
