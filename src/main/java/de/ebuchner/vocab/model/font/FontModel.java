package de.ebuchner.vocab.model.font;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.core.AbstractModel;

import java.util.HashMap;
import java.util.Map;

public class FontModel extends AbstractModel<FontModelChange, FontModelListener> {

    private static final String APP_FONT_PREFIX = "AppFont_";
    private Map<String, VocabFont> fontTypes = new HashMap<String, VocabFont>();

    public static FontModel getOrCreateFontModel() {
        return (FontModel) VocabModel.getInstance().getOrCreateModel(FontModel.class);
    }

    @Override
    protected void fireModelChanged() {
        for (FontModelListener listener : listeners) {
            listener.fontChanged();
        }
    }

    @Override
    public void reSynchronize() {
    }

    public void restoreFromPreferences(PreferenceValueList preferenceValues) {
        for (VocabFontType fontType : VocabFontType.values()) {
            String key = APP_FONT_PREFIX + fontType.name();

            FontValue fontValue = (FontValue) preferenceValues.getName(FontModel.class, key);

            if (fontValue != null)
                fontTypes.put(fontType.name(), fontValue.getFont());
        }
    }

    public void saveToPreferences(PreferenceValueList preferenceValues) {
        for (VocabFontType fontType : VocabFontType.values()) {
            String key = APP_FONT_PREFIX + fontType.name();
            preferenceValues.removeName(FontModel.class, key);

            VocabFont font = fontTypes.get(fontType.name());
            if (font != null)
                preferenceValues.putName(FontModel.class, key, new FontValue(font));
        }
    }

    public VocabFont getFont(VocabFontType fontType) {
        VocabFont font = fontTypes.get(fontType.name());
        if (font != null)
            return font;

        return fontTypes.get(VocabFontType.STANDARD.name());
    }

    public VocabFont getImageFont() {
        int fontSize = Config.instance().getImageFontSize();
        if (fontSize < 0)
            return null;
        VocabFont font = getFont(VocabFontType.VOCABULARY);
        if (font == null)
            return null;

        return new VocabFont(font.getName(), font.getStyle(), fontSize);
    }

    public String getFontSample() {
        return Config.instance().fontSample();
    }


    public void changeFont(VocabFontType vocabFontType, VocabFont vocabFont) {
        fontTypes.put(vocabFontType.name(), vocabFont);
        fireModelChanged();
    }

    public void resetFont(VocabFontType fontType) {
        fontTypes.remove(fontType.name());
        fireModelChanged();
    }
}
