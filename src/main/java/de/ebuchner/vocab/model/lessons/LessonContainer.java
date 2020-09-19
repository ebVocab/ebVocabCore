package de.ebuchner.vocab.model.lessons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LessonContainer extends Lesson {

    private List<Lesson> lessons = new ArrayList<Lesson>();

    public LessonContainer(File fileRef) {
        this(null, fileRef);
    }

    public LessonContainer(LessonContainer parent, File fileRef) {
        super(parent, fileRef);
    }

    protected LessonContainer(LessonContainer parent, File fileRef, LessonState state) {
        super(parent, fileRef, state);
    }

    public static LessonContainer createFrom(LessonContainer newParent, LessonContainer otherContainer) {
        LessonContainer container = new LessonContainer(
                newParent,
                otherContainer.getFileRef(),
                otherContainer.getState()
        );

        for (Lesson other : otherContainer.lessons()) {
            Lesson lesson;
            if (other.isContainer())
                lesson = LessonContainer.createFrom(container, other.asContainer());
            else
                lesson = Lesson.createFrom(container, other);
            container.addLessonElement(lesson);
        }

        return container;
    }

    public void addLessonElement(Lesson lesson) {
        lessons.add(lesson);
    }

    public Iterable<Lesson> lessons() {
        return lessons;
    }

    public boolean isRoot() {
        return getParent() == null;
    }

    @Override
    public String getName() {
        return getFileRef().getName();
    }

    public int size() {
        return lessons.size();
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    public Lesson findLessonByFileRef(File fileRef) {
        for (Lesson lesson : lessons()) {
            if (lesson.getFileRef().equals(fileRef))
                return lesson;
        }
        return null;
    }

    public void removeAll(List<Lesson> lessonsToRemove) {
        lessons.removeAll(lessonsToRemove);
    }
}
