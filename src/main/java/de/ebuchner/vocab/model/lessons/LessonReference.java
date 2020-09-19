package de.ebuchner.vocab.model.lessons;

import de.ebuchner.toolbox.lang.Equals;
import de.ebuchner.toolbox.lang.HashCode;

public class LessonReference {
    private Lesson lesson;
    private LessonState lessonState;

    public LessonReference(Lesson lesson) {
        this.lesson = lesson;
        this.lessonState = lesson.getState();
    }


    public Lesson getLesson() {
        return lesson;
    }

    public LessonState getLessonState() {
        return lessonState;
    }

    public void setLessonState(LessonState lessonState) {
        this.lessonState = lessonState;
    }

    @Override
    public int hashCode() {
        HashCode hc = new HashCode(this);
        hc.addObject(lesson);
        return hc.getResult();
    }

    @Override
    public boolean equals(Object o) {
        return new Equals(this).compareWith(o);
    }

    @Override
    public String toString() {
        return lesson.getName();
    }
}
