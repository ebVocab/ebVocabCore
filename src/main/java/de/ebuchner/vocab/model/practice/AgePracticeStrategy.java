package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;
import de.ebuchner.vocab.tools.ArrayRandomizer;

import java.util.*;

public class AgePracticeStrategy extends RandomCategorizedPracticeStrategy {
    @Override
    protected List<VocabEntryRef> randomizeEntryListImpl(
            List<VocabEntryRef> oldEntryRefList,
            VocabEntryRef oldCurrentRef
    ) {
        PracticeModel practiceModel = PracticeModel.getOrCreatePracticeModel();

        TreeMap<Integer, List<StatisticsEntry>> refMap =
                new TreeMap<Integer, List<StatisticsEntry>>();
        for (VocabEntryRef ref : oldEntryRefList) {
            StatisticsEntry entry = new StatisticsEntry(ref, practiceModel.statisticsValue(ref));
            List<StatisticsEntry> entryList = refMap.get(entry.age);
            if (entryList == null) {
                entryList = new ArrayList<StatisticsEntry>();
                refMap.put(entry.age, entryList);
            }
            entryList.add(entry);
        }

        List<Integer> sortedKeyList = new ArrayList<Integer>(refMap.keySet());

        List<VocabEntryRef> newRefList = new ArrayList<VocabEntryRef>();
        ArrayRandomizer<StatisticsEntry> randomizer = new ArrayRandomizer<StatisticsEntry>();

        for (ListIterator<Integer> iterator =
                     sortedKeyList.listIterator(
                             sortedKeyList.size()
                     ); iterator.hasPrevious(); ) {
            int key = iterator.previous();
            List<StatisticsEntry> statisticEntries =
                    randomizer.randomize(refMap.get(key));

            for (StatisticsEntry entry : statisticEntries) {
                newRefList.add(entry.ref);
            }
        }

        if (newRefList.size() != oldEntryRefList.size())
            throw new RuntimeException("assertion failed");

        return newRefList;
    }

    private static class StatisticsEntry {
        private final VocabEntryRef ref;
        private final Integer age;

        private StatisticsEntry(VocabEntryRef ref, PracticeStatisticsValue statisticsValue) {
            this.ref = ref;
            this.age = calculateAge(statisticsValue);
            if (age < 0)
                throw new IllegalArgumentException("Illegal statistics value " + age + " ~ " + new Date(statisticsValue.getTimestamp()) + " for " + ref);
        }

        private int calculateAge(PracticeStatisticsValue statisticsValue) {
            int newAge = Integer.MAX_VALUE;
            if (statisticsValue != null)
                newAge = (int) ((System.currentTimeMillis() - statisticsValue.getTimestamp()) / 1000 / 60 / 60 / 24);
            return newAge;
        }
    }
}
