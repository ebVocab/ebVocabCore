package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.fields.Field;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;

import java.io.File;

public class PracticeStateAskNext extends PracticeStateEnabled {

    public PracticeStateAskNext(PracticeStateHolder stateHolder) {
        super(stateHolder);
    }

    @Override
    protected PracticeStatisticsValue statisticsValueForDisplay() {
        return PracticeModel.getOrCreatePracticeModel().statisticsValue();
    }

    @Override
    protected boolean letStateDecideIfLessonIsShown(PracticeModel practiceModel) {
        return practiceModel.isShowLessonFile();
    }

    @Override
    public void stateNext() {
        stateHolder.setPracticeState(
                new PracticeStateShowSolution(
                        stateHolder,
                        PracticeModel.getOrCreatePracticeModel().statisticsValue()
                )
        );
    }

    @Override
    public void forceAskNext() {

    }

    @Override
    public void stateAskNext() {

    }

    @Override
    protected void updateFields(
            PracticeModel practiceModel,
            PracticeWindowBehaviour practiceWindow,
            File fileRef,
            VocabEntry currentEntry) {


        PracticeReverse reverse = practiceModel.getReverse();
        for (Field field : Config.instance().fieldListVisible()) {
            String value = currentEntry.getFieldValue(field.name());

            if (value == null)
                value = "";
            boolean fieldNotEmpty = value.trim().length() > 0;

            boolean fieldHidden = field.practiceHidden() && reverse == PracticeReverse.NORMAL;
            if (reverse == PracticeReverse.REVERSE)
                fieldHidden = field.practiceReverseHidden();

            if (fieldHidden)
                value = "";

            FieldRendererContext context = new FieldRendererContext(
                    fileRef,
                    field,
                    value,
                    fieldHidden,
                    fieldNotEmpty,
                    currentEntry
            );

            practiceWindow.renderField(context);
        }
    }

    @Override
    public boolean stateAskNextAvailable() {
        return false;
    }
}
