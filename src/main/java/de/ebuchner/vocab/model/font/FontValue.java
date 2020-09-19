package de.ebuchner.vocab.model.font;

import de.ebuchner.vocab.config.preferences.PreferenceValue;
import de.ebuchner.vocab.config.preferences.StringListValue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FontValue implements PreferenceValue {
    private static final Logger LOGGER = Logger.getLogger(FontValue.class.getName());
    private VocabFont font;

    public FontValue() {

    }

    public FontValue(VocabFont font) {
        this.font = font;
    }

    public String asString() {
        List<String> strings = new ArrayList<String>();
        strings.add(font.getName());
        strings.add(font.getStyle().toString());
        strings.add(String.valueOf(font.getSize()));

        return new StringListValue(strings).asString();
    }

    public void fromString(String stringValue) {
        StringListValue container = new StringListValue();
        container.fromString(stringValue);

        VocabFontStyle fontStyle;
        try {
            fontStyle = VocabFontStyle.valueOf(container.getStrings().get(1).toUpperCase());
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.toString(), e);
            fontStyle = VocabFontStyle.NORMAL;
        }

        this.font = new VocabFont(
                container.getStrings().get(0),
                fontStyle,
                Integer.parseInt(container.getStrings().get(2))
        );
    }

    public VocabFont getFont() {
        return font;
    }
}
