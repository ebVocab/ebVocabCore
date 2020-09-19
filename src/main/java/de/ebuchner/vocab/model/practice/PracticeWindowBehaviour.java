package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.fields.Field;
import de.ebuchner.vocab.model.lessons.RepetitionMode;

import java.io.File;

public interface PracticeWindowBehaviour {

    void setNavigationForwardEnabled(boolean enabled);

    void setNavigationBackwardEnabled(boolean enabled);

    void setFieldEnabled(Field field, boolean enabled);

    void renderField(FieldRendererContext context);

    void setStrategyOptionsEnabled(boolean enabled);

    void setSelectedStrategy(SelectedStrategy selectedStrategy);

    void setReverseOptionEnabled(boolean enabled);

    void setReverseOption(PracticeReverse reverseOption);

    void setImageOptionEnabled(boolean enabled);

    void setImageOption(PracticeImage imageOption);

    void setShowStatisticsOptionEnabled(boolean enabled);

    void setShowStatisticsOption(boolean showStatistics);

    void setLessonFieldsEnabled(boolean enabled);

    void setLessonFieldFileRef(File fileRef);

    void setLessonFieldShow(boolean showLessonFile);

    void setRepetitionModeEnabled(boolean enabled);

    void setRepetitionMode(RepetitionMode repetitionMode);

    void setRepetitionAddEnabled(boolean enabled);

    void setRepetitionAddVisible(boolean visible);

    void setRepetitionRemoveEnabled(boolean enabled);

    void setRepetitionRemoveVisible(boolean visible);

    void setRepetitionClearEnabled(boolean enabled);

    void setRepetitionClearVisible(boolean visible);

    void setRepetitionCount(int count);

    void setPracticeProgress(PracticeProgress progress);

    void firePracticeWindowOnFocusChanged();

    void firePracticeWindowOnUnFocus();

    void setNavigationReshuffleEnabled(boolean enabled);

    void setNavigationResetEnabled(boolean enabled);

    void askGotoPosition(int maxGotoPosition);

    void sendRepetitionSavedSuccess(File file);
}
