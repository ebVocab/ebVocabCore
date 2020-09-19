package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.fields.Field;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;

import java.io.File;

public class PracticeStateShowSolution extends PracticeStateEnabled {

    private final PracticeStatisticsValue oldStatistics;

    public PracticeStateShowSolution(PracticeStateHolder stateHolder, PracticeStatisticsValue oldStatistics) {
        super(stateHolder);
        this.oldStatistics = oldStatistics;
        PracticeModel practiceModel = PracticeModel.getOrCreatePracticeModel();
        practiceModel.updateStatistics();
    }

    @Override
    protected boolean letStateDecideIfLessonIsShown(PracticeModel practiceModel) {
        return true;
    }

    @Override
    public void stateNext() {
        stateHolder.onPrepareAskNext();
        stateAskNext();
    }

    @Override
    public void forceAskNext() {
        stateNext();
    }

    @Override
    protected void updateFields(
            PracticeModel practiceModel,
            PracticeWindowBehaviour practiceWindow,
            File fileRef,
            VocabEntry currentEntry) {

        for (Field field : Config.instance().fieldListVisible()) {
            String value = currentEntry.getFieldValue(field.name());

            if (value == null)
                value = "";

            FieldRendererContext context = new FieldRendererContext(
                    fileRef,
                    field,
                    value,
                    false,
                    value.length() > 0,
                    currentEntry
            );

            practiceWindow.renderField(context);
        }
    }

    @Override
    public void stateAskNext() {
        stateHolder.setPracticeState(new PracticeStateAskNext(stateHolder));
    }

    @Override
    public boolean stateAskNextAvailable() {
        return true;
    }

    @Override
    protected PracticeStatisticsValue statisticsValueForDisplay() {
        return oldStatistics;
    }
}
