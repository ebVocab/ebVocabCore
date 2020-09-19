package de.ebuchner.vocab.model.lessons;

import de.ebuchner.vocab.model.nui.NuiCloseEvent;

import java.util.List;

public class LessonController {

    private LessonWindowBehaviour lessonWindow;
    private LessonModel localCopyOfLessonModel;

    public LessonController(LessonWindowBehaviour lessonWindow) {
        LessonModel lessonModel = LessonModel.getOrCreateLessonModel();

        this.localCopyOfLessonModel = LessonModel.createFrom(lessonModel);
        this.lessonWindow = lessonWindow;
    }

    public void onWindowWasCreated() {
        lessonWindow.displayRoot(localCopyOfLessonModel.getRoot().getFileRef());
        lessonWindow.expandNonEmptyTreeRows();
        lessonWindow.scrollToFirstSelectedLesson();
        updateLessonWindow();
    }

    private void updateLessonWindow() {
        lessonWindow.displaySelectedLessons(
                localCopyOfLessonModel.countAvailableLessons(),
                localCopyOfLessonModel.countSelectedLessons()
        );
    }

    public LessonModelBehaviour getLessonModelBehaviour() {
        return localCopyOfLessonModel;
    }

    public void onLessonSelectionChanged(LessonReference reference) {
        LessonSelectionChange change = new LessonSelectionChange(reference.getLesson(), reference.getLessonState());
        localCopyOfLessonModel.executeCommand(change);
        onLessonTreeChanged();
    }

    public void onLessonTreeChanged() {
        updateLessonWindow();
    }

    public CheckResult onWindowClosing(NuiCloseEvent.CloseType closeType) {
        if (closeType == NuiCloseEvent.CloseType.OK) {
            /*
            if (localCopyOfLessonModel.countSelectedLessons() == 0) {
                lessonWindow.sendMessageSelectionEmpty();
                return CheckResult.Cancel;
            }
            */
            return CheckResult.CONTINUE;
        }

        if (localCopyOfLessonModel.isDirty())
            return lessonWindow.confirmCancel() ? CheckResult.CONTINUE : CheckResult.CANCEL;

        return CheckResult.CONTINUE;
    }

    public void onWindowClosed(NuiCloseEvent.CloseType closeType) {
        if (closeType == NuiCloseEvent.CloseType.OK) {
            LessonModel orgLessonModel = LessonModel.getOrCreateLessonModel();
            orgLessonModel.applyChanges(localCopyOfLessonModel);
        }
    }

    public void onSelectAll(boolean all) {
        LessonReference root = localCopyOfLessonModel.getRootReference();
        root.setLessonState(all ? LessonState.SELECTED : LessonState.UN_SELECTED);
        onLessonSelectionChanged(root);
    }

    public void onFilter() {
        int availableLessons = localCopyOfLessonModel.countAvailableLessons();
        LessonFilter filter = lessonWindow.filterPrompt(availableLessons);
        if (filter == null)
            return;

        List<Lesson> filteredLessons = localCopyOfLessonModel.getFilteredLessons(filter);
        if (filteredLessons.size() == 0)
            return;

        LessonReference root = localCopyOfLessonModel.getRootReference();
        localCopyOfLessonModel.executeCommand(
                new LessonSelectionChange(root.getLesson(), LessonState.UN_SELECTED)
        );

        for (Lesson lesson : filteredLessons) {
            localCopyOfLessonModel.executeCommand(
                    new LessonSelectionChange(lesson, LessonState.SELECTED)
            );
        }

        lessonWindow.collapseAll();
        lessonWindow.expandNonEmptyTreeRows();
        lessonWindow.scrollToFirstSelectedLesson();

        onLessonTreeChanged();
    }

    public enum CheckResult {
        CANCEL, CONTINUE
    }


}
