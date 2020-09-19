package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;
import de.ebuchner.vocab.tools.ArrayRandomizer;
import de.ebuchner.vocab.tools.FibonacciMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class FrequencyPracticeStrategy extends RandomCategorizedPracticeStrategy {
    @Override
    protected List<VocabEntryRef> randomizeEntryListImpl(
            List<VocabEntryRef> oldEntryRefList,
            VocabEntryRef oldCurrentRef
    ) {
        PracticeModel practiceModel = PracticeModel.getOrCreatePracticeModel();
        List<StatisticsEntry> statisticsEntries = new ArrayList<StatisticsEntry>();
        for (VocabEntryRef ref : oldEntryRefList) {
            statisticsEntries.add(new StatisticsEntry(ref, practiceModel.statisticsValue(ref)));
        }

        Map<Integer, List<StatisticsEntry>> refMap =
                new FibonacciMap<StatisticsEntry>().mapFrom(statisticsEntries);
        TreeSet<Integer> sortedKeySet = new TreeSet<Integer>(refMap.keySet());

        List<VocabEntryRef> newRefList = new ArrayList<VocabEntryRef>();
        ArrayRandomizer<StatisticsEntry> randomizer = new ArrayRandomizer<StatisticsEntry>();

        // refMap is ordered by usage count. Randomize each group separately and add it to the output list
        for (int key : sortedKeySet) {
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

    private static class StatisticsEntry implements FibonacciMap.IntValueHaving {
        private final VocabEntryRef ref;
        private final PracticeStatisticsValue statisticsValue;

        private StatisticsEntry(VocabEntryRef ref, PracticeStatisticsValue statisticsValue) {
            this.ref = ref;
            this.statisticsValue = statisticsValue;
        }

        public int value() {
            if (statisticsValue != null)
                return statisticsValue.getUsageCount();
            return 0;
        }
    }
}
