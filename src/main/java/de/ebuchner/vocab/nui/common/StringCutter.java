package de.ebuchner.vocab.nui.common;

public class StringCutter {

    private StringCutter() {

    }

    public static String ensureLength(String value, int maxLen) {
        if (maxLen <= 0)
            throw new IllegalArgumentException("maxLen must be greater than 0");

        if (value == null)
            return null;

        if (value.length() > maxLen) {
            int cutIndexLeft = maxLen / 2 - 3;
            int cutIndexRight = value.length() - maxLen / 2;
            value = value.substring(0, cutIndexLeft) + "..." + value.substring(cutIndexRight);
        }
        return value;
    }
}
