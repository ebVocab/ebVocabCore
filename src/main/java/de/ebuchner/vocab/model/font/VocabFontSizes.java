package de.ebuchner.vocab.model.font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VocabFontSizes {

    private static final List<Integer> FONT_SIZES = Arrays.asList(
            48, 36, 28, 26, 24, 22, 20, 18, 16, 14, 12, 11, 10, 9, 8
    );

    private VocabFontSizes() {

    }

    public static List<Integer> fontSizes() {
        List<Integer> copy = new ArrayList<Integer>();
        copy.addAll(FONT_SIZES);
        return copy;
    }

    public static VocabFont increaseFont(VocabFont font) {
        return changeFont(font, +1);
    }

    public static VocabFont decreaseFont(VocabFont font) {
        return changeFont(font, -1);
    }

    private static VocabFont changeFont(VocabFont font, int delta) {
        int newSize = font.getSize() + delta;
        int foundSize = 0;

        for (int fontSize : fontSizes()) {

            if (delta > 0) {
                if (fontSize >= newSize) {
                    if (foundSize == 0 || fontSize < foundSize)
                        foundSize = fontSize;
                }
            } else if (delta < 0) {
                if (fontSize <= newSize) {
                    if (foundSize == 0 || fontSize > foundSize)
                        foundSize = fontSize;
                }
            }
        }

        if (foundSize == 0)
            return font;

        return new VocabFont(font.getName(), font.getStyle(), foundSize);
    }
}
