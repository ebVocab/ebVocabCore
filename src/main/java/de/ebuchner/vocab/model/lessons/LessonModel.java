package de.ebuchner.vocab.model.lessons;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.ConfigConstants;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.config.preferences.StringListValue;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.commands.CompositeCommand;
import de.ebuchner.vocab.model.core.AbstractModel;
import de.ebuchner.vocab.model.core.ModelChangeEvent;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

public class LessonModel
        extends AbstractModel<LessonSelectionChange, LessonSelectionChangeListener>
        implements LessonModelBehaviour {

    private static final String PREFERENCES_FILE_REFS = "FILE_REFS";
    private LessonContainer root;
    private FileFilter lessonFilter = new FileFilter() {

        public boolean accept(File file) {
            return file.isDirectory() ||
                    file.getName().endsWith(ConfigConstants.FILE_EXTENSION) ||
                    file.getName().endsWith(ConfigConstants.FILE_REF_EXTENSION);
        }
    };

    public LessonModel() {
        this(Config.instance().getProjectInfo().getVocabDirectory());
    }

    public LessonModel(File lessonRoot) {
        if (!lessonRoot.exists() || !lessonRoot.isDirectory())
            throw new IllegalArgumentException("Not a directory " + lessonRoot);

        this.root = new LessonContainer(null, lessonRoot);

        recursiveLoadFrom(root, lessonRoot);
    }

    private LessonModel(LessonContainer root) {
        this.root = root;
        reSynchronize();
    }

    public static LessonModel getOrCreateLessonModel() {
        return (LessonModel) VocabModel.getInstance().getOrCreateModel(LessonModel.class);
    }

    public static LessonModel createFrom(LessonModel otherModel) {
        otherModel.reSynchronize();
        return new LessonModel(LessonContainer.createFrom(null, otherModel.getRoot()));
    }

    private void recursiveLoadFrom(LessonContainer root, File lessonRoot) {
        for (File file : lessonRoot.listFiles(lessonFilter)) {
            if (file.isDirectory()) {
                LessonContainer container = new LessonContainer(root, file);
                recursiveLoadFrom(container, file);
                root.addLessonElement(container);
            } else {
                Lesson lesson = new Lesson(root, file);
                root.addLessonElement(lesson);
            }
        }
    }

    public LessonContainer getRoot() {
        return root;
    }

    public int countSelectedLessons() {
        return countSelectedLessons(root);
    }

    private int countSelectedLessons(Lesson lesson) {
        if (lesson.isContainer()) {
            int count = 0;
            for (Lesson child : lesson.asContainer().lessons()) {
                count += countSelectedLessons(child);
            }
            return count;
        } else
            return lesson.getState() == LessonState.SELECTED ? 1 : 0;
    }

    public void restoreFromPreferences(PreferenceValueList preferences) {
        StringListValue fileRefs = (StringListValue) preferences.getName(LessonModel.class, PREFERENCES_FILE_REFS);
        if (fileRefs == null)
            return;

        CompositeCommand command = new CompositeCommand();
        restoreFromPreferences(root, command, fileRefs);
        command.execute();
    }

    private void restoreFromPreferences(Lesson lesson, CompositeCommand command, StringListValue fileRefs) {
        if (lesson.isContainer()) {
            for (Lesson child : lesson.asContainer().lessons()) {
                restoreFromPreferences(child, command, fileRefs);
            }
        } else {
            String fileRef;
            try {
                fileRef = lesson.getFileRef().getCanonicalPath();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (fileRefs.getStrings().contains(fileRef))
                command.getCommands().add(new LessonSelectionChange(lesson, LessonState.SELECTED));
        }
    }

    public void saveToPreferences(PreferenceValueList preferences) {
        List<String> fileRefs = new ArrayList<>();

        saveToPreferences(root, fileRefs);

        StringListValue value = new StringListValue(fileRefs);
        preferences.putName(LessonModel.class, PREFERENCES_FILE_REFS, value);
    }

    private void saveToPreferences(Lesson lesson, List<String> fileRefs) {
        if (lesson.isContainer()) {
            for (Lesson child : lesson.asContainer().lessons()) {
                saveToPreferences(child, fileRefs);
            }
        } else {
            if (lesson.getState() == LessonState.SELECTED)
                try {
                    fileRefs.add(lesson.getFileRef().getCanonicalPath());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
    }

    public List<Lesson> getSelectedLessons() {
        List<Lesson> selectedLessons = new ArrayList<>();
        addSelectedLessons(selectedLessons, root);
        return selectedLessons;
    }

    private void addSelectedLessons(List<Lesson> selectedLessons, Lesson lesson) {
        if (lesson.isContainer()) {
            for (Lesson child : lesson.asContainer().lessons()) {
                addSelectedLessons(selectedLessons, child);
            }
        } else {
            if (lesson.getState() == LessonState.SELECTED)
                selectedLessons.add(lesson);
        }
    }

    @Override
    protected void fireModelChanged() {
        List<Lesson> affectedLessons = new ArrayList<>();
        for (LessonSelectionChange change : undoableCommands()) {
            affectedLessons.addAll(change.getAffectedLessons());
        }

        ModelChangeEvent event = new ModelChangeEvent(this);
        for (LessonSelectionChangeListener listener : listeners) {
            listener.lessonChanged(event, affectedLessons);
        }
    }

    @Override
    public void reSynchronize() {
        super.clearCommands();

        File rootFileRef = getRoot().getFileRef();
        if (!rootFileRef.exists()) {
            this.root = new LessonContainer(null, rootFileRef);
            return;
        }

        LessonModel updatedLessonModel = new LessonModel(this.getRoot().getFileRef());
        reSynchronize(getRoot(), updatedLessonModel.getRoot());
    }

    private void reSynchronize(LessonContainer myContainer, LessonContainer updatedContainer) {
        // find all new lessons
        for (Lesson updatedLesson : updatedContainer.lessons()) {
            Lesson myLesson = myContainer.findLessonByFileRef(updatedLesson.getFileRef());
            if (myLesson == null) {
                if (updatedLesson.isContainer()) {
                    LessonContainer myNewSubContainer =
                            new LessonContainer(myContainer, updatedLesson.getFileRef());
                    myContainer.addLessonElement(myNewSubContainer);
                    recursiveLoadFrom(myNewSubContainer, updatedLesson.getFileRef());
                } else
                    myContainer.addLessonElement(new Lesson(myContainer, updatedLesson.getFileRef(), LessonState.UN_SELECTED));
            } else {
                if (myLesson.isContainer()) {
                    reSynchronize(myLesson.asContainer(), updatedLesson.asContainer());
                }
            }
        }

        // remove all lessons that do not longer exist
        List<Lesson> lessonsToRemove = new ArrayList<>();
        for (Lesson myLesson : myContainer.lessons()) {
            if (updatedContainer.findLessonByFileRef(myLesson.getFileRef()) == null)
                lessonsToRemove.add(myLesson);
        }
        myContainer.removeAll(lessonsToRemove);
    }

    public void applyChanges(LessonModel otherModel) {
        List<LessonSelectionChange> commands = new ArrayList<>();
        for (LessonSelectionChange otherCommand : otherModel.undoableCommands()) {
            Lesson otherLesson = otherCommand.getLesson();
            Lesson lesson = findLesson(otherLesson.getFileRef());
            if (lesson == null)
                throw new IllegalArgumentException("Incompatible lesson models: " + otherLesson.getFileRef());
            commands.add(new LessonSelectionChange(lesson, otherCommand.getState()));
        }
        executeCommands(commands);
    }

    private Lesson findLesson(File fileRef) {
        return findLesson(root, fileRef);
    }

    private Lesson findLesson(Lesson lesson, File fileRef) {
        if (lesson.getFileRef().equals(fileRef))
            return lesson;
        if (lesson.isContainer()) {
            for (Lesson child : lesson.asContainer().lessons()) {
                Lesson lessonResult = findLesson(child, fileRef);
                if (lessonResult != null)
                    return lessonResult;
            }
        }
        return null;
    }

    public LessonReference getRootReference() {
        return new LessonReference(getRoot());
    }

    public int countAvailableLessons() {
        return countAvailableLessons(root);
    }

    private int countAvailableLessons(Lesson lesson) {
        if (lesson.isContainer()) {
            int count = 0;
            for (Lesson child : lesson.asContainer().lessons()) {
                count += countAvailableLessons(child);
            }
            return count;
        } else
            return 1;
    }

    public List<Lesson> getFilteredLessons(LessonFilter filter) {
        List<Lesson> allLessons = new ArrayList<>();
        addLessons(allLessons, root);

        Set<Lesson> sortedLessons = new TreeSet<>(new Comparator<Lesson>() {
            @Override
            public int compare(Lesson l1, Lesson l2) {
                File f1 = l1.getFileRef();
                File f2 = l2.getFileRef();

                if (f1 == null || f2 == null)
                    return 0;

                // sort descending, i.e. f2.compareTo(f1)
                return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
            }
        }
        );
        sortedLessons.addAll(allLessons);

        List<Lesson> filtered = new ArrayList<>(sortedLessons);
        int filterCnt = filter.getNumberOfRecentFiles();
        if (filterCnt <= filtered.size())
            return filtered.subList(0, filter.getNumberOfRecentFiles());
        return Collections.emptyList();
    }

    private void addLessons(List<Lesson> lessons, Lesson lesson) {
        if (lesson.isContainer()) {
            for (Lesson child : lesson.asContainer().lessons()) {
                addLessons(lessons, child);
            }
        } else
            lessons.add(lesson);
    }

}
