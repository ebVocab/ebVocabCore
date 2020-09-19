package de.ebuchner.vocab.model.lessons;

import de.ebuchner.vocab.config.preferences.PreferenceValue;
import de.ebuchner.vocab.config.preferences.StringListValue;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VocabEntryRefListPreferenceValue implements PreferenceValue {

    private List<VocabEntryRef> vocabEntryRefList = new ArrayList<VocabEntryRef>();

    public String asString() {
        List<String> stringValues = new ArrayList<String>();
        for (VocabEntryRef ref : vocabEntryRefList) {
            try {
                stringValues.add(ref.getFileRef().getCanonicalPath());
                stringValues.add(ref.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        StringListValue prefHelper = new StringListValue(stringValues);
        return prefHelper.asString();
    }

    public void fromString(String stringValue) {
        vocabEntryRefList.clear();

        StringListValue prefHelper = new StringListValue();
        prefHelper.fromString(stringValue);

        if (prefHelper.getStrings().size() % 2 != 0)
            throw new RuntimeException("Unexpected value");

        for (int i = 0; i < prefHelper.getStrings().size(); i += 2) {
            String fileRef = prefHelper.getStrings().get(i);
            String id = prefHelper.getStrings().get(i + 1);

            vocabEntryRefList.add(
                    new VocabEntryRef(
                            new File(fileRef),
                            id
                    )
            );
        }
    }

    public List<VocabEntryRef> getVocabEntryRefList() {
        return vocabEntryRefList;
    }
}
