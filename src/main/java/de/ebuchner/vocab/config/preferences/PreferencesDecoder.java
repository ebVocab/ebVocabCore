package de.ebuchner.vocab.config.preferences;

import de.ebuchner.vocab.tools.StreamBuffer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

/*public*/ class PreferencesDecoder implements PreferencesIOConstants {


    public PreferenceValueList decode(InputStream inputStream) {
        DecoderXmlHandler xmlHandler = new DecoderXmlHandler();
        try {
            // SAXParser closes automatically the input stream -> create duplicate
            InputStream inputStreamDecorator = new StreamBuffer(inputStream).duplicateInputStream();

            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(inputStreamDecorator, xmlHandler);

            return xmlHandler.values;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    static class DecoderXmlHandler extends DefaultHandler {
        PreferenceValueList values = new PreferenceValueList();
        StringBuilder className = new StringBuilder();
        StringBuilder prefName = new StringBuilder();
        StringBuilder prefValue = new StringBuilder();
        private Mode currentMode;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            currentMode = Mode.NONE;

            if (qName.equals(ELEMENT_ENTRY))
                currentMode = Mode.IN_ENTRY;
            else if (qName.equals(ELEMENT_CLASS_NAME))
                currentMode = Mode.IN_CLASS_NAME;
            else if (qName.equals(ELEMENT_PREFERENCE_NAME))
                currentMode = Mode.IN_PREFERENCE_NAME;
            else if (qName.equals(ELEMENT_PREFERENCE_VALUE))
                currentMode = Mode.IN_PREFERENCE_VALUE;

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals(ELEMENT_ENTRY)) {
                PreferenceValue value;
                try {
                    value = (PreferenceValue) Class.forName(XmlUtils.fromXml(className.toString())).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                value.fromString(XmlUtils.fromXml(prefValue.toString()));
                values.putName(XmlUtils.fromXml(prefName.toString()), value);

                className = new StringBuilder();
                prefName = new StringBuilder();
                prefValue = new StringBuilder();
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (currentMode == Mode.IN_CLASS_NAME)
                className.append(ch, start, length);
            else if (currentMode == Mode.IN_PREFERENCE_NAME)
                prefName.append(ch, start, length);
            else if (currentMode == Mode.IN_PREFERENCE_VALUE)
                prefValue.append(ch, start, length);
        }

        private enum Mode {
            NONE, IN_ENTRY, IN_PREFERENCE_NAME, IN_PREFERENCE_VALUE, IN_CLASS_NAME
        }
    }
}
