package de.ebuchner.vocab.config.preferences;

class XmlUtils {

    private static final String FF_UNESCAPED = "\f";
    private static final String FF_ESCAPED = "{{\\f}}";

    private XmlUtils() {

    }

    static String toXml(String input) {
        if (input == null)
            return null;
        return input.replace(FF_UNESCAPED, FF_ESCAPED);
    }

    static String fromXml(String input) {
        if (input == null)
            return null;
        return input.replace(FF_ESCAPED, FF_UNESCAPED);
    }
}
