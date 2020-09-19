package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.preferences.IntegerValue;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrowsePracticeStrategy extends AbstractPracticeStrategy {

    protected final static int EMPTY = Integer.MAX_VALUE;
    private static final String POSITION = "position";
    protected List<VocabEntryRef> entryRefList = new ArrayList<VocabEntryRef>();
    protected int position = EMPTY;

    private Integer cachedPosition = null;

    @Override
    public boolean containsEntryRef(VocabEntryRef entryRef) {
        return entryRefList.contains(entryRef);
    }

    @Override
    protected void clearImpl() {
        entryRefList.clear();
        position = EMPTY;
    }

    @Override
    public int getPracticeSize() {
        return entryRefList.size();
    }

    @Override
    protected void reShuffleImpl() {
    }

    @Override
    protected void resetImpl() {
        resetPosition();
    }

    @Override
    public boolean supportsGotoPositions() {
        return true;
    }

    @Override
    protected void gotoPositionImpl(int newPosition) {
        if (position == EMPTY)
            return;
        if (newPosition < 0 || newPosition >= getPracticeSize())
            return;
        position = newPosition;
    }

    protected final void resetPosition() {
        position = 0;
        if (getPracticeSize() == 0)
            position = EMPTY;
    }

    @Override
    protected List<VocabEntryRef> uniqueEntries() {
        List<VocabEntryRef> uniqueRefList = new ArrayList<VocabEntryRef>();
        for (VocabEntryRef ref : entryRefList) {
            if (uniqueRefList.contains(ref))
                continue;
            uniqueRefList.add(ref);
        }
        return uniqueRefList;
    }

    @Override
    protected void addModelRef(VocabEntryRef ref) {
        entryRefList.add(ref);
        if (position == EMPTY)
            position = 0;
    }

    @Override
    protected boolean removeModelRef(VocabEntryRef ref) {
        boolean removed = false;
        while (true) {
            int positionToRemove = entryRefList.indexOf(ref);
            if (positionToRemove < 0)
                return removed;

            removed = true;

            if (position >= positionToRemove && position > 0) {
                // only when positioned at the last entry shift position to previous - otherwise remain on position
                if (position == entryRefList.size() - 1)
                    position--;
            }
            entryRefList.remove(positionToRemove);
            if (entryRefList.isEmpty())
                position = EMPTY;
        }
    }

    @Override
    public VocabEntryRef currentRef() {
        if (position == EMPTY)
            return null;

        return entryRefList.get(position);
    }

    @Override
    protected void gotoNextRef() {
        if (position == EMPTY)
            return;

        position++;
        if (position >= entryRefList.size())
            position = 0;
    }

    @Override
    protected void gotoPreviousRef() {
        if (position == EMPTY)
            return;

        position--;
        if (position < 0)
            position = entryRefList.size() - 1;
    }

    @Override
    public int getPracticePosition() {
        return position;
    }

    /*package only*/ List<VocabEntryRef> clipAt(int clipLength) {
        if (clipLength < 0 || clipLength > entryRefList.size())
            return Collections.emptyList();
        if (clipLength == 0) {
            List<VocabEntryRef> clippedRefs = new ArrayList<VocabEntryRef>();
            clippedRefs.addAll(entryRefList);
            clearImpl();
            return clippedRefs;
        }

        List<VocabEntryRef> clippedRefs = new ArrayList<VocabEntryRef>();
        while (entryRefList.size() > clipLength) {
            clippedRefs.add(entryRefList.remove(clipLength));
            if (position >= entryRefList.size())
                position = entryRefList.size() - 1;
        }
        return clippedRefs;
    }

    /*package only*/ void setPosition(int newPosition) {
        position = newPosition;
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
        IntegerValue positionValue = new IntegerValue(position);
        preferenceValues.putName(this.getClass(), POSITION, positionValue);
    }

    @Override
    public void reSynchronize() {
        super.reSynchronize();

        boolean positionChanged =
                (cachedPosition != null &&
                        cachedPosition < entryRefList.size() &&
                        cachedPosition != position
                );

        if (positionChanged)
            position = cachedPosition;

        cachedPosition = null;

        if (positionChanged)
            fireModelChanged();
    }
}
