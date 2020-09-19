package de.ebuchner.vocab.model.transliteration;

import java.util.HashMap;
import java.util.Map;

class TransliterationCodes {

    // http://de.wikipedia.org/wiki/Unicode-Block_Kombinierende_diakritische_Zeichen
    private static final String TILDE = "\u0303";
    private static final String MACRON = "\u0304";
    private static final String ACUTE = "\u0301";
    private static final String DOT_ABOVE = "\u0307";
    private static final String DOT_BELOW = "\u0323";
    private static final String UNDERLINE = "\u0332";
    private static final String CEDILLA = "\u0327";
    private static final String CIRCUMFLEX = "\u0302";
    private static final String GRAVE = "\u0300";
    private static final String DIAERESIS = "\u0308";

    private static final String CAPITAL_OE = "\u0152";
    private static final String SMALL_OE = "\u0153";

    private static final String CAPITAL_AE = "\u00C6";
    private static final String SMALL_AE = "\u00E6";

    Map<String, String> codes = new HashMap<String, String>();

    public TransliterationCodes() {
        codes.put("TILDE", TILDE);
        codes.put("MACRON", MACRON);
        codes.put("ACUTE", ACUTE);
        codes.put("DOT_ABOVE", DOT_ABOVE);
        codes.put("DOT_BELOW", DOT_BELOW);
        codes.put("UNDERLINE", UNDERLINE);
        codes.put("CEDILLA", CEDILLA);
        codes.put("CIRCUMFLEX", CIRCUMFLEX);
        codes.put("GRAVE", GRAVE);
        codes.put("DIAERESIS", DIAERESIS);

        codes.put("CAPITAL_OE", CAPITAL_OE);
        codes.put("SMALL_OE", SMALL_OE);

        codes.put("CAPITAL_AE", CAPITAL_AE);
        codes.put("SMALL_AE", SMALL_AE);
    }

    public String replaceCodes(String string) {
        if (string == null)
            return null;

        for (String code : codes.keySet()) {
            string = string.replaceAll("\\$\\{" + code + "\\}", codes.get(code));
        }

        return string;
    }
}
