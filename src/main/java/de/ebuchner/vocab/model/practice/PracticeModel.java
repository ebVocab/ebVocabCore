package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.preferences.BooleanValue;
import de.ebuchner.vocab.config.preferences.PreferenceValue;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.config.preferences.StringValue;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.core.AbstractModel;
import de.ebuchner.vocab.model.core.ModelChangeEvent;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

public class PracticeModel extends AbstractModel<PracticeModelChange, PracticeModelListener> {

    private static final String PREFERENCES_SELECTED_STRATEGY = "SELECTED_STRATEGY";
    private static final String PREFERENCES_REVERSE = "REVERSE";
    private static final String PREFERENCES_SHOW_LESSON_FILE = "SHOW_LESSON_FILE";
    private static final String PREFERENCES_SHOW_STATISTICS = "SHOW_STATISTICS";
    private static final String PREFERENCES_IMAGE = "SHOW_IMAGE";

    private SelectedStrategy selectedStrategy = SelectedStrategy.RANDOM;
    private PracticeReverse reverse = PracticeReverse.NORMAL;
    private PracticeImage imageOption = PracticeImage.IMAGE;
    private boolean showLessonFile = false;

    private boolean showStatistics = true;
    private VocabEntryRef currentRef;

    public static PracticeModel getOrCreatePracticeModel() {
        return (PracticeModel) VocabModel.getInstance().getOrCreateModel(PracticeModel.class);
    }


    @Override
    protected void fireModelChanged() {
        ModelChangeEvent event = new ModelChangeEvent(this);
        for (PracticeModelListener listener : listeners) {
            listener.practiceChanged(event);
        }
    }

    public void restoreFromPreferences(PreferenceValueList preferenceValues) {
        PreferenceValue strategyValue = preferenceValues.getName(
                PracticeModel.class,
                PREFERENCES_SELECTED_STRATEGY
        );
        if (strategyValue != null) {
            selectedStrategy = SelectedStrategy.valueOf(strategyValue.asString().toUpperCase());
            if (selectedStrategy == null)
                selectedStrategy = SelectedStrategy.RANDOM;
        }

        BooleanValue reverseValue = (BooleanValue) preferenceValues.getName(
                PracticeModel.class,
                PREFERENCES_REVERSE
        );
        if (reverseValue != null) {
            reverse = PracticeReverse.NORMAL;
            if (reverseValue.getValue())
                reverse = PracticeReverse.REVERSE;
        }

        BooleanValue showLessonValue = (BooleanValue) preferenceValues.getName(
                PracticeModel.class,
                PREFERENCES_SHOW_LESSON_FILE
        );
        if (showLessonValue != null)
            showLessonFile = showLessonValue.getValue();

        BooleanValue showStatisticsValue = (BooleanValue) preferenceValues.getName(
                PracticeModel.class,
                PREFERENCES_SHOW_STATISTICS
        );
        if (showStatisticsValue != null)
            showStatistics = showStatisticsValue.getValue();

        BooleanValue imageValue = (BooleanValue) preferenceValues.getName(
                PracticeModel.class,
                PREFERENCES_IMAGE
        );
        if (imageValue != null) {
            imageOption = PracticeImage.IMAGE;
            if (!imageValue.getValue())
                imageOption = PracticeImage.TEXT;
        }

        fireModelChanged();
    }

    public void saveToPreferences(PreferenceValueList preferenceValues) {
        StringValue strategyValue = new StringValue(selectedStrategy.name());
        preferenceValues.putName(
                PracticeModel.class,
                PREFERENCES_SELECTED_STRATEGY,
                strategyValue
        );

        BooleanValue reverseValue = new BooleanValue(reverse == PracticeReverse.REVERSE);
        preferenceValues.putName(
                PracticeModel.class,
                PREFERENCES_REVERSE,
                reverseValue
        );

        BooleanValue showLessonValue = new BooleanValue(showLessonFile);
        preferenceValues.putName(
                PracticeModel.class,
                PREFERENCES_SHOW_LESSON_FILE,
                showLessonValue
        );

        BooleanValue showStatisticsValue = new BooleanValue(showStatistics);
        preferenceValues.putName(
                PracticeModel.class,
                PREFERENCES_SHOW_STATISTICS,
                showStatisticsValue
        );

        BooleanValue imageValue = new BooleanValue(imageOption == PracticeImage.IMAGE);
        preferenceValues.putName(
                PracticeModel.class,
                PREFERENCES_IMAGE,
                imageValue
        );
    }

    PracticeReverse getReverse() {
        return reverse;
    }

    void setReverse(PracticeReverse reverse) {
        this.reverse = reverse;
        fireModelChanged();
    }

    public SelectedStrategy getSelectedStrategy() {
        return selectedStrategy;
    }

    void setSelectedStrategy(SelectedStrategy selectedStrategy) {
        this.selectedStrategy = selectedStrategy;
        fireModelChanged();
    }

    boolean isShowLessonFile() {
        return showLessonFile;
    }

    void setShowLessonFile(boolean showLessonFile) {
        this.showLessonFile = showLessonFile;
        fireModelChanged();
    }

    @Override
    public void reSynchronize() {

    }

    public VocabEntryRef getCurrentRef() {
        return currentRef;
    }

    public void setCurrentRef(VocabEntryRef currentRef) {
        this.currentRef = currentRef;
    }

    public void updateStatistics() {
        if (currentRef == null)
            return;

        String id = currentRef.getId();

        PreferenceValueList statistics = Config.instance().statistics().getPreferenceValueList();
        PracticeStatisticsValue statisticsValue = (PracticeStatisticsValue) statistics.getName(id);
        if (statisticsValue == null) {
            statisticsValue = new PracticeStatisticsValue();
            statisticsValue.setRefId(id);
            statistics.putName(id, statisticsValue);
        }
        statisticsValue.incrementUsage();
    }

    public void clearCurrentFile() {
        this.currentRef = null;
    }

    public boolean hasCurrentRef() {
        return this.currentRef != null;
    }

    public PracticeStatisticsValue statisticsValue() {
        if (!hasCurrentRef())
            return null;
        return statisticsValue(currentRef);
    }

    public PracticeStatisticsValue statisticsValue(VocabEntryRef ref) {
        PreferenceValueList statistics = Config.instance().statistics().getPreferenceValueList();
        PracticeStatisticsValue statisticsValue = (PracticeStatisticsValue) statistics.getName(ref.getId());
        if (statisticsValue == null)
            return null;
        return statisticsValue.createCopy();
    }

    public boolean isShowStatistics() {
        return showStatistics;
    }

    public void setShowStatistics(boolean showStatistics) {
        this.showStatistics = showStatistics;
    }

    public PracticeImage getImageOption() {
        return imageOption;
    }

    public void setImageOption(PracticeImage imageOption) {
        this.imageOption = imageOption;
    }
}
