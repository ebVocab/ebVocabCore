package de.ebuchner.vocab.model.keyboard;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyMap {

    private final String locale;
    private List<KeyMapEntry> entries = new ArrayList<KeyMapEntry>();
    private String keyboardName;

    private KeyMap(String locale) {
        this.locale = locale;
    }

    public static KeyMap fromLocale(String locale) {
        KeyMap keyMap = new KeyMap(locale);

        URL keyMapURL = KeyMap.class.getResource("/de/ebuchner/vocab/model/keyboard/keyMap_" + locale + ".xml");
        if (keyMapURL == null)
            return null;

        KeyEncoderXmlHandler xmlHandler = keyMap.new KeyEncoderXmlHandler();

        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(keyMapURL.openStream(), xmlHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return keyMap;
    }

    public String getLocale() {
        return locale;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

    public String getKeyboardName() {
        return keyboardName;
    }

    public List<KeyMapEntry> keyMapEntries() {
        return Collections.unmodifiableList(entries);
    }

    private class KeyEncoderXmlHandler extends DefaultHandler {

        private static final String ELEMENT_KEY = "key";
        private static final String ROOT_KEY = "keyMap";
        private static final String ATTRIBUTE_TEXT = "text";
        private static final String ATTRIBUTE_CHARS = "chars";
        private static final String ATTRIBUTE_KEY_ROW = "row";
        private static final String ATTRIBUTE_KEY_COLUMN = "column";
        private static final String ATTRIBUTE_KEY_CAPITAL_ONLY = "capitalOnly";
        private static final String ELEMENT_KEY_MODE = "keyMode";
        private static final String ATTRIBUTE_KEY_MODE_SHIFT = "shift";
        private static final String ATTRIBUTE_KEY_MODE_ALT_GR = "altGR";
        private static final String ELEMENT_KEY_MODE_DISPLAY = "display";
        private static final String ELEMENT_KEY_MODE_GENERATE = "generate";
        private static final String ATTRIBUTE_ROOT_NAME = "name";

        private KeyMapEntry keyMapEntry;
        private KeyMode keyMode;

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) {
            if (qName.equals(ROOT_KEY)) {
                keyboardName = getAttributeValue(attributes, ATTRIBUTE_ROOT_NAME);
            } else if (qName.equals(ELEMENT_KEY)) {
                keyMapEntry = new KeyMapEntry(locale);
                keyMapEntry.setRow(Integer.parseInt(getAttributeValue(attributes, ATTRIBUTE_KEY_ROW)));
                keyMapEntry.setColumn(Integer.parseInt(getAttributeValue(attributes, ATTRIBUTE_KEY_COLUMN)));
                keyMapEntry.setCapitalOnly("true".equals(getAttributeValue(attributes, ATTRIBUTE_KEY_CAPITAL_ONLY)));
                entries.add(keyMapEntry);
            } else if (qName.equals(ELEMENT_KEY_MODE)) {
                keyMode = new KeyMode(locale);

                boolean shift = "true".equals(getAttributeValue(attributes, ATTRIBUTE_KEY_MODE_SHIFT));
                boolean altGR = "true".equals(getAttributeValue(attributes, ATTRIBUTE_KEY_MODE_ALT_GR));
                keyMode.setModifier(determineKeyModifier(shift, altGR));

                keyMapEntry.getKeyModes().add(keyMode);
            } else if (qName.equals(ELEMENT_KEY_MODE_DISPLAY)) {
                keyMode.setDisplayText(getAttributeValue(attributes, ATTRIBUTE_TEXT));
                if (isEmpty(keyMode.getDisplayText()))
                    keyMode.setDisplayText(fromUnicode(getAttributeValue(attributes, ATTRIBUTE_CHARS)));
            } else if (qName.equals(ELEMENT_KEY_MODE_GENERATE)) {
                keyMode.setGenerateText(getAttributeValue(attributes, ATTRIBUTE_TEXT));
                if (isEmpty(keyMode.getGenerateText()))
                    keyMode.setGenerateText(fromUnicode(getAttributeValue(attributes, "chars")));
            }
        }

        private String getAttributeValue(Attributes attributes, String name) {
            int idIdx = attributes.getIndex(name);
            if (idIdx >= 0)
                return attributes.getValue(idIdx);
            return null;
        }

        private KeyModifier determineKeyModifier(boolean shift, boolean altGR) {
            if (altGR && shift)
                return KeyModifier.SHIFT_ALT_GR;
            if (altGR)
                return KeyModifier.ALT_GR;
            if (shift)
                return KeyModifier.SHIFT;

            return KeyModifier.NORMAL;
        }

        private String fromUnicode(String code) {
            if (isEmpty(code))
                return "";
            if (!code.startsWith("\\u"))
                return code;

            int intCode = Integer.parseInt(code.substring(2), 16);
            return Character.toString((char) intCode);
        }
    }
}
