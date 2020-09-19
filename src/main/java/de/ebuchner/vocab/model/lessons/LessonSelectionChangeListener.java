package de.ebuchner.vocab.model.lessons;

import de.ebuchner.vocab.model.core.ModelChangeEvent;
import de.ebuchner.vocab.model.core.ModelListener;

import java.util.List;

public interface LessonSelectionChangeListener extends ModelListener {
    void lessonChanged(ModelChangeEvent event, List<Lesson> affectedLessons);
}
