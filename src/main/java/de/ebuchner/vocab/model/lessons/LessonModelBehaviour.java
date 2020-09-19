package de.ebuchner.vocab.model.lessons;

public interface LessonModelBehaviour {
    LessonReference getRootReference();

    int countAvailableLessons();

    void addListener(LessonSelectionChangeListener listener);

    void removeListener(LessonSelectionChangeListener listener);
}
