package de.ebuchner.vocab.model.lessons;

import de.ebuchner.vocab.config.fields.FieldFactory;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyEntrySupport {

    private LegacyEntrySupport() {

    }

    public static List<Map<String, String>> toRawList(VocabEntryList entryList) {
        List<Map<String, String>> rawList = new ArrayList<Map<String, String>>();
        for (VocabEntry entry : entryList.entries()) {
            Map<String, String> rawEntry = new HashMap<String, String>();
            rawList.add(rawEntry);

            for (String fieldName : entry.fieldNames()) {
                rawEntry.put(fieldName, entry.getFieldValue(fieldName));
            }
            rawEntry.put(FieldFactory.ID, entry.getId());
        }
        return rawList;
    }

    public static VocabEntryList toVocab(List<Map<String, String>> rawList) {
        VocabEntryList entryList = new VocabEntryList();

        for (Map<String, String> rawEntry : rawList) {
            String ID = rawEntry.get(FieldFactory.ID);

            VocabEntry vocabEntry = ID != null ? new VocabEntry(ID) : new VocabEntry();
            entryList.addEntry(vocabEntry);

            for (String rawName : rawEntry.keySet()) {
                if (rawName.equals(FieldFactory.ID))
                    continue;

                String rawValue = rawEntry.get(rawName);
                vocabEntry.putFieldValue(rawName, rawValue);
            }
        }

        return entryList;
    }
}
