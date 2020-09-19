package de.ebuchner.vocab.model.transliteration;

import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.commands.SimpleCommand;
import de.ebuchner.vocab.model.core.AbstractModel;
import de.ebuchner.vocab.model.core.ModelListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TransliterationModel extends AbstractModel<SimpleCommand, ModelListener> {

    private List<String> transliterationCharacters = new ArrayList<String>();
    private TransliterationCodes codes = new TransliterationCodes();

    public TransliterationModel() {
        loadModel();
    }

    public static TransliterationModel getOrCreateTransliterationModel() {
        return (TransliterationModel) VocabModel.getInstance().getOrCreateModel(TransliterationModel.class);
    }

    private void loadModel() {
        try {
            URL url = new TransliterationAvailable().transliterationURL();

            BufferedReader r = new BufferedReader(
                    new InputStreamReader(
                            url.openStream(),
                            "utf-8"
                    )
            );
            try {
                String line;
                while ((line = r.readLine()) != null) {
                    line = line.trim();
                    if (line.length() == 0)
                        continue;

                    transliterationCharacters.add(codes.replaceCodes(line));
                }
            } finally {
                r.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public int rows() {
        return columns();
    }

    public int columns() {
        return (int) Math.ceil(Math.sqrt(transliterationCharacters.size()));
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

    public int size() {
        return transliterationCharacters.size();
    }

    public String characterAt(int characterIndex) {
        return transliterationCharacters.get(characterIndex);
    }
}
