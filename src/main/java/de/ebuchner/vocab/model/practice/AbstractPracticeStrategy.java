package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.core.AbstractModel;
import de.ebuchner.vocab.model.core.ModelChangeEvent;
import de.ebuchner.vocab.model.lessons.VocabEntryModel;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

import java.util.List;

public abstract class AbstractPracticeStrategy
        extends AbstractModel<StrategyChange, StrategyChangeListener> {

    public static AbstractPracticeStrategy getOrCreateStrategyModel(SelectedStrategy selectedStrategy) {
        Class<? extends AbstractPracticeStrategy> strategyClass;
        if (selectedStrategy == SelectedStrategy.BROWSE)
            strategyClass = BrowsePracticeStrategy.class;
        else if (selectedStrategy == SelectedStrategy.RANDOM)
            strategyClass = RandomPracticeStrategy.class;
        else if (selectedStrategy == SelectedStrategy.INTENSE)
            strategyClass = IntensePracticeStrategy.class;
        else if (selectedStrategy == SelectedStrategy.AGE)
            strategyClass = AgePracticeStrategy.class;
        else if (selectedStrategy == SelectedStrategy.FREQUENCY)
            strategyClass = FrequencyPracticeStrategy.class;
        else
            throw new IllegalArgumentException("Unknown strategy type " + selectedStrategy.name());

        return (AbstractPracticeStrategy) VocabModel.getInstance().getOrCreateModel(strategyClass);
    }

    public abstract boolean containsEntryRef(VocabEntryRef entryRef);

    public final void clear() {
        super.clearCommands();
        clearImpl();
    }

    protected abstract void clearImpl();

    public abstract VocabEntryRef currentRef();

    protected abstract void gotoNextRef();

    protected abstract void gotoPreviousRef();

    public final void reShuffle() {
        super.clearCommands();
        reShuffleImpl();
        fireModelChanged();
    }

    protected abstract void reShuffleImpl();

    public void restoreFromPreferences(PreferenceValueList preferenceValues) {

    }

    public void saveToPreferences(PreferenceValueList preferenceValues) {

    }

    @Override
    protected void fireModelChanged() {
        ModelChangeEvent event = new ModelChangeEvent(this);
        for (StrategyChangeListener listener : listeners) {
            listener.strategyChanged(event);
        }
    }

    protected abstract boolean removeModelRef(VocabEntryRef ref);

    protected abstract void addModelRef(VocabEntryRef ref);

    @Override
    public void reSynchronize() {
        VocabEntryModel vocabEntryModel = VocabEntryModel.getOrCreateVocabEntryModel();
        List<VocabEntryRef> modelRefs = vocabEntryModel.getAllReferences();
        List<VocabEntryRef> strategyRefs = uniqueEntries();

        int changeCount = 0;

        for (VocabEntryRef strategyRef : strategyRefs) {
            if (!modelRefs.contains(strategyRef)) {
                removeModelRef(strategyRef);
                changeCount++;
            }
        }

        for (VocabEntryRef modelRef : modelRefs) {
            if (!strategyRefs.contains(modelRef)) {
                addModelRef(modelRef);
                changeCount++;
            }
        }

        if (changeCount > 0)
            fireModelChanged();
    }

    // always return a copy (!) of entries to avoid ConcurrentModificationExceptions
    protected abstract List<VocabEntryRef> uniqueEntries();

    public VocabEntry currentEntry() {
        VocabEntry currentEntry = VocabEntryModel.getOrCreateVocabEntryModel().findEntry(currentRef());
        if (currentEntry == null)
            removeModelRef(currentRef());
        return currentEntry;
    }

    public abstract int getPracticePosition();

    public abstract int getPracticeSize();

    public final void reset() {
        super.clearCommands();
        resetImpl();
        fireModelChanged();
    }

    protected abstract void resetImpl();

    public abstract boolean supportsGotoPositions();

    public final void gotoPosition(int newPosition) {
        super.clearCommands();
        gotoPositionImpl(newPosition);
        fireModelChanged();
    }

    protected abstract void gotoPositionImpl(int newPosition);
}
