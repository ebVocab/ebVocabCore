package de.ebuchner.vocab.model.lessons;

import de.ebuchner.vocab.model.commands.SimpleCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonSelectionChange implements SimpleCommand {

    private Lesson lesson;
    private LessonState state;
    private Map<Lesson, LessonState> oldStates = new HashMap<>();
    private List<Lesson> affectedLessons = new ArrayList<>();

    public LessonSelectionChange(Lesson lesson, LessonState state) {
        this.lesson = lesson;
        this.state = state;

        if (state != LessonState.UN_SELECTED && state != LessonState.SELECTED)
            throw new IllegalArgumentException("Cannot change state to " + state);
    }

    public void execute() {
        changeTo(lesson, state);
    }

    public void unExecute() {
        affectedLessons.clear();
        for (Lesson lesson : oldStates.keySet()) {
            LessonState oldState = oldStates.get(lesson);
            lesson.setState(oldState);
            affectedLessons.add(lesson);
        }
        oldStates.clear();
    }

    private void changeStateUndoable(Lesson lesson, LessonState state) {
        oldStates.put(lesson, lesson.getState());
        lesson.setState(state);
        affectedLessons.add(lesson);
    }

    private void changeTo(Lesson lesson, LessonState state) {
        changeStateUndoable(lesson, state);
        if (lesson.isContainer())
            changeChildrenTo(lesson.asContainer(), state);

        adjustParent(lesson);
    }

    private void adjustParent(Lesson lesson) {
        LessonState parentState;

        LessonContainer parent = lesson.getParent();
        if (parent == null) {
            return;
        }

        int numChildrenSelected = 0;
        int numChildrenPartially = 0;
        int numChildren = 0;

        for (Lesson child : parent.lessons()) {
            numChildren++;
            if (child.getState() == LessonState.SELECTED)
                numChildrenSelected++;
            else if (child.getState() == LessonState.PARTIALLY_SELECTED)
                numChildrenPartially++;
        }

        if (numChildrenSelected == numChildren)
            parentState = LessonState.SELECTED;
        else if (numChildrenSelected > 0 || numChildrenPartially > 0)
            parentState = LessonState.PARTIALLY_SELECTED;
        else
            parentState = LessonState.UN_SELECTED;

        changeStateUndoable(parent, parentState);
        adjustParent(parent);
    }

    private void changeChildrenTo(LessonContainer lesson, LessonState state) {
        for (Lesson child : lesson.asContainer().lessons()) {
            changeStateUndoable(child, state);
            if (child.isContainer()) {
                changeChildrenTo(child.asContainer(), state);
            }
        }

    }

    @Override
    public String toString() {
        return "Changing " + lesson.getName() + " to " + state.name();
    }

    public List<Lesson> getAffectedLessons() {
        return affectedLessons;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public LessonState getState() {
        return state;
    }
}
