package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.config.preferences.StringListValue;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;
import de.ebuchner.vocab.tools.ArrayRandomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPracticeStrategy extends BrowsePracticeStrategy {

    private static final String ENTRY_ID_LIST = "entry_id_list";

    protected Random random = new Random(System.currentTimeMillis());
    private List<String> cachedIdList;

    @Override
    protected void reShuffleImpl() {
        super.reShuffleImpl();

        resetPosition();
        randomizeEntryList();
    }

    @Override
    public boolean supportsGotoPositions() {
        return false;
    }

    private void randomizeEntryList() {
        List<VocabEntryRef> oldEntryRefList = new ArrayList<VocabEntryRef>(entryRefList);
        VocabEntryRef oldCurrentRef = currentRef();

        entryRefList.clear();
        entryRefList.addAll(
                randomizeEntryListImpl(
                        oldEntryRefList,
                        oldCurrentRef
                )
        );
    }

    protected List<VocabEntryRef> randomizeEntryListImpl(
            List<VocabEntryRef> oldEntryRefList,
            VocabEntryRef oldCurrentRef
    ) {
        // avoid that the next (first) vocab entry is the same as the current after randomizing the list
        String oldCurrentId = null;

        if (oldEntryRefList.size() > 1)
            oldCurrentId = oldCurrentRef.getId();

        List<VocabEntryRef> newRefList;

        while (true) {
            newRefList = new ArrayRandomizer<VocabEntryRef>().randomize(oldEntryRefList);

            if (oldCurrentId == null)
                break;

            String newNextId = newRefList.get(0).getId();

            if (!newNextId.equals(oldCurrentId))
                break;
        }
        return newRefList;
    }

    @Override
    protected void gotoNextRef() {
        // randomize each time all vocabs have been practiced
        if (position == getPracticeSize() - 1)
            randomizeEntryList();
        super.gotoNextRef();
    }

    @Override
    protected void addModelRef(VocabEntryRef ref) {
        super.addModelRef(ref);
        // base class always adds references at the and
        reShuffleImpl();
    }

    @Override
    public void saveToPreferences(PreferenceValueList preferenceValues) {
        super.saveToPreferences(preferenceValues);
        if (!isCacheVocabList())
            return;
        List<String> entryIdList = new ArrayList<String>();

        for (VocabEntryRef ref : entryRefList) {
            entryIdList.add(ref.getId());
        }
        StringListValue entryIdListValue = new StringListValue(entryIdList);

        preferenceValues.putName(this.getClass(), ENTRY_ID_LIST, entryIdListValue);
    }

    @Override
    public void restoreFromPreferences(PreferenceValueList preferenceValues) {
        super.restoreFromPreferences(preferenceValues);
        if (!isCacheVocabList())
            return;
        StringListValue entryIdListValue = (StringListValue) preferenceValues.getName(this.getClass(), ENTRY_ID_LIST);
        if (entryIdListValue == null)
            return;

        cachedIdList = entryIdListValue.getStrings();
    }

    protected boolean isCacheVocabList() {
        return true;
    }

    @Override
    public void reSynchronize() {
        super.reSynchronize();

        if (cachedIdList == null)
            return;

        List<String> idList = new ArrayList<String>(cachedIdList);
        cachedIdList = null;

        if (idList.size() != entryRefList.size())
            return;

        List<VocabEntryRef> newEntryRefList = new ArrayList<VocabEntryRef>();

        for (String id : idList) {
            VocabEntryRef nextEntry = null;
            entrySearch:
            for (VocabEntryRef entry : entryRefList) {
                if (entry.getId().equals(id)) {
                    nextEntry = entry;
                    break entrySearch;
                }
            }
            if (nextEntry == null)
                return;

            newEntryRefList.add(nextEntry);
        }

        entryRefList.clear();
        entryRefList.addAll(newEntryRefList);

        fireModelChanged();
    }
}
