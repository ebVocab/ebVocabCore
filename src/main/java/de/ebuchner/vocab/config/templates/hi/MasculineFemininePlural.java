package de.ebuchner.vocab.config.templates.hi;

public class MasculineFemininePlural {

    private static final String MASCULINE = "m";
    private static final String FEMININE = "f";

    public boolean isMasculine(String value) {
        return MASCULINE.equals(value);
    }

    public boolean isFeminine(String value) {
        return FEMININE.equals(value);
    }

    public String doFeminine(String hindi) {
        if (hindi == null || hindi.length() == 0)
            return hindi;

        char lastChar = hindi.charAt(hindi.length() - 1);

        String plural;

        // vowel sign "i"
        if (hindi.endsWith("\u093f") || hindi.endsWith("\u0940"))
            // vowel sign "i" + ya + vowel sign a + chandrabindu
            plural = hindi.substring(0, hindi.length() - 1) + "\u093f\u092f\u093e\u0901";
            // vowel sign "i" + Ya + vowel sign "a"
        else if (hindi.endsWith("\u093f\u092f\u093e"))
            // s.o.
            plural = hindi.substring(0, hindi.length() - 3) + "\u093f\u092f\u093e\u0901";
            // vowel sign "Uu"
        else if (hindi.endsWith("\u0942"))
            // vowel sign "u" + e + chandrabindu
            plural = hindi.substring(0, hindi.length() - 1) + "\u0941\u090f\u0901";
            // vowel or vowel sign
        else if ((lastChar >= 0x0905 && lastChar <= 0x0914) ||
                ((lastChar >= 0x093e && lastChar <= 0x094c)))
            // e + chandrabindu
            plural = hindi + "\u090f\u0901";
        else
            // vowel sign "e" + anusvara
            plural = hindi + "\u0947\u0902";

        return plural;
    }

    public String doMasculine(String hindi) {
        if (hindi == null || hindi.length() == 0)
            return hindi;

        String plural = hindi;
        // long "a" vowel sign
        if (hindi.endsWith("\u093E"))
            // "e" vowel sign
            plural = hindi.substring(0, hindi.length() - 1) + "\u0947";

        return plural;
    }

}
