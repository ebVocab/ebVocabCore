package de.ebuchner.vocab.model.lessons;

import de.ebuchner.vocab.config.preferences.PreferenceValue;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.config.preferences.StringValue;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.core.AbstractModel;
import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

import java.io.File;
import java.util.*;

public class VocabEntryModel extends AbstractModel<VocabEntryModelCommand, VocabEntryModelListener> {

    private static final String REPETITION_MODE = "repetition_mode";
    private static final String REPETITION_REFERENCES = "repetition_references";

    private Map<File, VocabEntryList> allEntries = new HashMap<File, VocabEntryList>();
    private List<VocabEntryRef> allLessonReferences = new ArrayList<VocabEntryRef>();
    private List<VocabEntryRef> allRepetitionReferences = new ArrayList<VocabEntryRef>();

    private RepetitionMode repetitionMode = RepetitionMode.OFF;

    public static VocabEntryModel getOrCreateVocabEntryModel() {
        return (VocabEntryModel) VocabModel.getInstance().getOrCreateModel(VocabEntryModel.class);
    }

    public List<VocabEntryRef> getAllReferences() {
        if (repetitionMode == RepetitionMode.OFF)
            return Collections.unmodifiableList(allLessonReferences);
        return getRepetitionReferences();
    }

    public void reSynchronize() {
        allEntries.clear();
        allLessonReferences.clear();

        LessonModel lessonModel = LessonModel.getOrCreateLessonModel();
        for (Lesson lesson : lessonModel.getSelectedLessons()) {
            File lessonFile = lesson.getFileRef();
            VocabEntryList entryList = VocabIOHelper.fromFile(lessonFile);
            if (entryList == null) // legacy file
                continue;
            allEntries.put(lessonFile, entryList);
            for (VocabEntry entry : entryList.entries()) {
                allLessonReferences.add(VocabEntryRef.fromEntry(lesson, entry));
            }
        }

        reloadRepetitionEntries();
        fireModelChanged();
    }

    private void reloadRepetitionEntries() {
        List<VocabEntryRef> invalidReferences = new ArrayList<VocabEntryRef>();
        for (VocabEntryRef ref : allRepetitionReferences) {
            VocabEntryList entryList = allEntries.get(ref.getFileRef());
            if (entryList == null) {
                try {
                    entryList = VocabIOHelper.fromFile(ref.getFileRef());
                    allEntries.put(ref.getFileRef(), entryList);

                    VocabEntry entry = entryList.findEntry(ref);
                    if (entry == null)
                        invalidReferences.add(ref);
                } catch (Exception e) {
                    invalidReferences.add(ref);
                }
            }
        }
        if (!invalidReferences.isEmpty()) {
            System.out.println("Ignored invalid repetition references:");
            for (VocabEntryRef invalidRef : invalidReferences) {
                System.out.println(invalidRef);
            }
        }
    }

    @Override
    protected void fireModelChanged() {
        fireModelChanged(VocabEntryChangedEvent.ChangeType.ACTIVE_SET);
    }

    protected void fireModelChanged(VocabEntryChangedEvent.ChangeType changeType) {
        VocabEntryChangedEvent event = new VocabEntryChangedEvent(changeType);
        for (VocabEntryModelListener listener : listeners) {
            listener.vocabEntryModelChanged(event);
        }
    }

    public void restoreFromPreferences(PreferenceValueList preferenceValues) {
        PreferenceValue repetitionValue = preferenceValues.getName(
                VocabEntryModel.class,
                REPETITION_MODE
        );
        if (repetitionValue != null)
            repetitionMode = RepetitionMode.valueOf(repetitionValue.asString().toUpperCase());

        VocabEntryRefListPreferenceValue refListValue = (VocabEntryRefListPreferenceValue) preferenceValues.getName(
                VocabEntryModel.class,
                REPETITION_REFERENCES
        );
        if (refListValue != null) {
            for (VocabEntryRef entryRef : refListValue.getVocabEntryRefList()) {
                if (entryRef.getFileRef() != null && entryRef.getFileRef().exists())
                    allRepetitionReferences.add(entryRef);
            }
            if (allRepetitionReferences.isEmpty())
                repetitionMode = RepetitionMode.OFF;
            else
                reloadRepetitionEntries();
        }
    }

    public void saveToPreferences(PreferenceValueList preferenceValues) {
        StringValue repetitionValue = new StringValue(repetitionMode.name());

        preferenceValues.putName(
                VocabEntryModel.class,
                REPETITION_MODE,
                repetitionValue
        );

        VocabEntryRefListPreferenceValue refListValue = new VocabEntryRefListPreferenceValue();
        refListValue.getVocabEntryRefList().addAll(allRepetitionReferences);

        preferenceValues.putName(
                VocabEntryModel.class,
                REPETITION_REFERENCES,
                refListValue
        );
    }

    public VocabEntry findEntry(VocabEntryRef entryRef) {
        VocabEntryList entryList = allEntries.get(entryRef.getFileRef());
        if (entryList == null)
            return null;
        return entryList.findEntry(entryRef);
    }

    public RepetitionMode getRepetitionMode() {
        return repetitionMode;
    }

    public void setRepetitionMode(RepetitionMode repetitionMode) {
        if (repetitionMode == RepetitionMode.ON && !hasRepetitions())
            throw new IllegalStateException("Cannot switch to repetition mode");

        this.repetitionMode = repetitionMode;
        fireModelChanged(VocabEntryChangedEvent.ChangeType.ACTIVE_SET);

        // more specialized events probably more useful than general events?
        for (VocabEntryModelListener listener : listeners) {
            listener.repetitionModeActivated(repetitionMode);
        }
    }

    public boolean hasRepetitions() {
        return !allRepetitionReferences.isEmpty();
    }

    public boolean isInRepetition(VocabEntryRef entryRef) {
        return allRepetitionReferences.contains(entryRef);
    }

    public void addRepetition(VocabEntryRef entryRef) {
        if (allRepetitionReferences.contains(entryRef))
            return;

        allRepetitionReferences.add(entryRef);
        fireModelChanged(VocabEntryChangedEvent.ChangeType.INACTIVE_SET);
    }

    // buggy! Repetition autosave file sollte referenzen enthalten keine entries, sonst geht der Bezug
    // zum urspruenglichen File verloren und als lektion steht nur noch autosave file
    // und pruefen ob die referenzen noch gueltig sind!
    public void loadRepetitions(File repetitionFile) {
        VocabEntryList repetitions = VocabIOHelper.fromFile(repetitionFile);
        allRepetitionReferences.clear();
        for (VocabEntry entry : repetitions.entries())
            allRepetitionReferences.add(new VocabEntryRef(repetitionFile, entry.getId()));

        fireModelChanged(VocabEntryChangedEvent.ChangeType.INACTIVE_SET);
    }

    public void removeRepetition(VocabEntryRef entryRef) {
        if (!allRepetitionReferences.contains(entryRef))
            return;

        allRepetitionReferences.remove(entryRef);
        VocabEntryChangedEvent.ChangeType changeType = VocabEntryChangedEvent.ChangeType.INACTIVE_SET;
        if (allRepetitionReferences.isEmpty()) {
            repetitionMode = RepetitionMode.OFF;
            changeType = VocabEntryChangedEvent.ChangeType.ACTIVE_SET;
        } else if (repetitionMode == RepetitionMode.ON)
            changeType = VocabEntryChangedEvent.ChangeType.ACTIVE_SET;
        fireModelChanged(changeType);
    }

    public void clearRepetition() {
        allRepetitionReferences.clear();
        VocabEntryChangedEvent.ChangeType changeType = VocabEntryChangedEvent.ChangeType.INACTIVE_SET;
        if (repetitionMode == RepetitionMode.ON) {
            repetitionMode = RepetitionMode.OFF;
            changeType = VocabEntryChangedEvent.ChangeType.ACTIVE_SET;
        }
        fireModelChanged(changeType);
    }

    public int repetitionCount() {
        return allRepetitionReferences.size();
    }

    public boolean saveRepetitions(File file) {
        if (allRepetitionReferences.size() == 0)
            return false;

        VocabIOHelper.toRefFile(file, allRepetitionReferences);

        return true;
    }

    public List<VocabEntryRef> getRepetitionReferences() {
        return Collections.unmodifiableList(allRepetitionReferences);
    }

    public void setRepetitions(List<VocabEntryRef> entryRefs) {
        List<VocabEntryRef> validRefs = new ArrayList<>();


        for (VocabEntryRef ref : entryRefs) {
            boolean validReference = true;
            VocabEntryList entryList = VocabIOHelper.fromFile(ref.getFileRef());
            if (entryList == null)
                validReference = false;
            else {
                VocabEntry entry = entryList.findEntry(ref);
                if (entry == null)
                    validReference = false;
            }
            if (validReference)
                validRefs.add(ref);
            else
                System.out.format("Ignored invalid repetition references: %s", ref);

        }
        if (validRefs.size() == 0)
            return;

        allRepetitionReferences.clear();
        allRepetitionReferences.addAll(validRefs);

        if (getRepetitionMode() == RepetitionMode.ON)
            fireModelChanged(VocabEntryChangedEvent.ChangeType.ACTIVE_SET);
        else
            fireModelChanged(VocabEntryChangedEvent.ChangeType.INACTIVE_SET);
    }
}
