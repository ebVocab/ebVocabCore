package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.preferences.IntegerValue;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

import java.util.ArrayList;
import java.util.List;

public class IntensePracticeStrategy extends AbstractPracticeStrategy {

    private static final String POSITION = "position";
    private static final int EMPTY = -1;

    private int groupSize;
    private int groupRepetition;

    private IntenseRefGroups intenseRefGroups = new IntenseRefGroups();
    private List<VocabEntryRef> simpleRefs = new ArrayList<VocabEntryRef>();
    private int intensePos = EMPTY;

    private Integer cachedPosition = null;

    public IntensePracticeStrategy() {
        this(3, 3);
    }

    public IntensePracticeStrategy(int groupSize, int groupRepetition) {
        this.groupSize = groupSize;
        this.groupRepetition = groupRepetition;
    }

    @Override
    public boolean containsEntryRef(VocabEntryRef entryRef) {
        return simpleRefs.contains(entryRef);
    }

    @Override
    protected void clearImpl() {
        simpleRefs.clear();
        intenseRefGroups.clear();
        intensePos = EMPTY;
    }

    @Override
    public VocabEntryRef currentRef() {
        if (intensePos < 0)
            return null;

        return intenseRefGroups.getRef(intensePos);
    }

    @Override
    protected void gotoNextRef() {
        if (intensePos < 0)
            return;

        intensePos++;
        if (intensePos >= intenseRefGroups.size())
            intensePos = 0;
    }

    @Override
    protected void gotoPreviousRef() {
        if (intensePos < 0)
            return;
        intensePos--;
        if (intensePos < 0)
            intensePos = intenseRefGroups.size() - 1;
    }

    @Override
    protected void reShuffleImpl() {
        if (intensePos < 0)
            return;

        intensePos = intenseRefGroups.nextGroupIndex(intensePos);
    }

    @Override
    protected void addModelRef(VocabEntryRef ref) {
        simpleRefs.add(ref);
        intenseRefGroups.clear();

        int simpleIndex = 0;
        int modifiedGroupSize = groupSize;
        while (simpleIndex < simpleRefs.size()) {
            for (int repetition = 0; repetition < groupRepetition; repetition++) {
                if (repetition == 0)
                    intenseRefGroups.addGroup();
                for (int groupIndex = 0; groupIndex < modifiedGroupSize; groupIndex++) {
                    int currentSimpleIndex = simpleIndex + groupIndex;
                    if (currentSimpleIndex < simpleRefs.size()) {
                        intenseRefGroups.addEntry(simpleRefs.get(currentSimpleIndex));
                    }
                }
            }
            simpleIndex += modifiedGroupSize;
            if (simpleRefs.size() - simpleIndex < 2 * groupSize) // the last group is smaller than groupSize
                modifiedGroupSize = simpleRefs.size() - simpleIndex;
        }

        if (intensePos < 0)
            intensePos = 0;
    }

    @Override
    protected boolean removeModelRef(VocabEntryRef ref) {
        if (intensePos < 0)
            return false;

        boolean result = simpleRefs.remove(ref);

        intensePos = intenseRefGroups.removeEntry(intensePos, ref);

        return result;
    }

    @Override
    protected List<VocabEntryRef> uniqueEntries() {
        return new ArrayList<VocabEntryRef>(simpleRefs);
    }

    @Override
    public int getPracticePosition() {
        //return intensePos;
        VocabEntryRef currentRef = currentRef();
        if (currentRef == null)
            return EMPTY;

        int pos = 0;

        for (VocabEntryRef ref : simpleRefs) {
            if (ref.getId().equals(currentRef.getId()))
                return pos;
            pos++;
        }

        System.out.println("ERROR: Current position not found");
        return 0;
    }

    @Override
    public int getPracticeSize() {
        //return intenseRefGroups.size();
        return simpleRefs.size();
    }

    /*package only*/ void setPosition(int newPosition) {
        intensePos = newPosition;
    }

    @Override
    public void restoreFromPreferences(PreferenceValueList preferenceValues) {
        IntegerValue positionValue = (IntegerValue) preferenceValues.getName(this.getClass(), POSITION);
        if (positionValue == null)
            return;

        cachedPosition = positionValue.getValue();
    }

    @Override
    public void saveToPreferences(PreferenceValueList preferenceValues) {
        IntegerValue positionValue = new IntegerValue(intensePos);
        preferenceValues.putName(this.getClass(), POSITION, positionValue);
    }

    @Override
    public void reSynchronize() {
        super.reSynchronize();

        boolean positionChanged =
                (cachedPosition != null &&
                        cachedPosition < intenseRefGroups.size() &&
                        cachedPosition != intensePos
                );

        if (positionChanged)
            intensePos = cachedPosition;

        cachedPosition = null;

        if (positionChanged)
            fireModelChanged();
    }

    @Override
    protected void resetImpl() {
        intensePos = 0;
        if (getPracticeSize() == 0)
            intensePos = EMPTY;

    }

    @Override
    public boolean supportsGotoPositions() {
        return false;
    }

    @Override
    protected void gotoPositionImpl(int newPosition) {
        throw new UnsupportedOperationException();
    }

    private static class IntenseRefGroups {
        List<List<VocabEntryRef>> groups = new ArrayList<List<VocabEntryRef>>();

        public void clear() {
            groups.clear();
        }

        public VocabEntryRef getRef(int pos) {
            int currentPos = EMPTY;
            for (List<VocabEntryRef> refList : groups) {
                for (VocabEntryRef ref : refList) {
                    currentPos++;
                    if (currentPos == pos)
                        return ref;
                }
            }
            return null;
        }

        public int size() {
            int size = 0;
            for (List<VocabEntryRef> refList : groups) {
                size += refList.size();
            }
            return size;
        }

        public int nextGroupIndex(int previousPos) {
            if (previousPos < 0)
                return previousPos;

            int nextPos = 0;
            for (List<VocabEntryRef> refList : groups) {
                nextPos = nextPos + refList.size();
                previousPos -= refList.size();
                if (previousPos < 0) {
                    if (nextPos >= size())
                        return 0;
                    return nextPos;
                }
            }
            return 0;
        }

        public void addGroup() {
            groups.add(new ArrayList<VocabEntryRef>());
        }

        public void addEntry(VocabEntryRef vocabEntryRef) {
            List<VocabEntryRef> currentList = groups.get(groups.size() - 1);
            currentList.add(vocabEntryRef);
        }

        public int removeEntry(int oldIntensePos, VocabEntryRef ref) {
            int newIntensePos = oldIntensePos;
            int currentPos = EMPTY;
            for (List<VocabEntryRef> group : groups) {
                for (VocabEntryRef entryRef : group) {
                    currentPos++;
                    if (ref.equals(entryRef)) {
                        if (currentPos < newIntensePos)
                            newIntensePos--;
                    }
                }
                while (group.remove(ref)) {

                }
            }

            List<List<VocabEntryRef>> newGroups = new ArrayList<List<VocabEntryRef>>();
            for (List<VocabEntryRef> group : groups) {
                if (!group.isEmpty())
                    newGroups.add(group);
            }
            groups = newGroups;

            return newIntensePos;
        }
    }
}
