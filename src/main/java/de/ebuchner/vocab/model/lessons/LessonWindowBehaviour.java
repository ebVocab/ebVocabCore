package de.ebuchner.vocab.model.lessons;

import java.io.File;

public interface LessonWindowBehaviour {
    void displayRoot(File rootDirectory);

    void expandNonEmptyTreeRows();

    void scrollToFirstSelectedLesson();

    void displaySelectedLessons(int availableLessons, int selectedLessons);

    void sendMessageSelectionEmpty();

    boolean confirmCancel();

    LessonFilter filterPrompt(int availableLessons);

    void collapseAll();
}
