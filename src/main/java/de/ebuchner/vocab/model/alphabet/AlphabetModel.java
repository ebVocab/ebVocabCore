package de.ebuchner.vocab.model.alphabet;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.fields.FieldFactory;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.core.AbstractModel;
import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AlphabetModel extends AbstractModel implements Alphabet {
    private List<AlphabetCharacter> alphabetList;

    public AlphabetModel() {
        this(
                new AlphabetAvailable().alphabetResource(
                        Config.instance().getLocale()
                )
        );
    }

    public AlphabetModel(URL alphabetLessonResource) {
        VocabEntryList alphabetEntries = VocabIOHelper.fromURL(alphabetLessonResource);
        alphabetList = new ArrayList<AlphabetCharacter>();

        for (VocabEntry entry : alphabetEntries.entries()) {
            AlphabetCharacter character = new AlphabetCharacter(
                    entry.getFieldValue(FieldFactory.FOREIGN),
                    entry.getFieldValue(FieldFactory.USER)
            );
            alphabetList.add(character);
        }
    }

    public static AlphabetModel getOrCreateAlphabetModel() {
        return (AlphabetModel) VocabModel.getInstance().getOrCreateModel(AlphabetModel.class);
    }

    public Iterator<AlphabetCharacter> iterator() {
        return alphabetList.iterator();
    }

    public int size() {
        return alphabetList.size();
    }

    public AlphabetCharacter characterAt(int pos) {
        return alphabetList.get(pos);
    }

    @Override
    protected void fireModelChanged() {

    }

    @Override
    public void reSynchronize() {

    }

    public void restoreFromPreferences(PreferenceValueList preferenceValues) {

    }

    public void saveToPreferences(PreferenceValueList preferenceValues) {

    }
}
