package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.config.fields.FieldFactory;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;
import de.ebuchner.vocab.tools.StreamBuffer;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldDecoder implements VocabIOConstants {

    public VocabEntryList decode(InputStream inputStream) {
        FieldEncoderXmlHandler xmlHandler = new FieldEncoderXmlHandler();

        try {
            // SAXParser closes automatically the input stream -> create duplicate
            InputStream inputStreamDecorator = new StreamBuffer(inputStream).duplicateInputStream();

            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(inputStreamDecorator, xmlHandler);

            return xmlHandler.entries;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public VocabEntryList decodeFromString(String entriesAsString) {
        FieldEncoderXmlHandler xmlHandler = new FieldEncoderXmlHandler();

        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(new InputSource(new StringReader(entriesAsString)), xmlHandler);

            return xmlHandler.entries;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class FieldEncoderXmlHandler extends DefaultHandler {

        private StringBuffer fieldLabelBuf = new StringBuffer();
        private StringBuffer fieldValueBuf = new StringBuffer();
        private List<String> validFieldNames = FieldFactory.getGenericFieldNameList();
        private Mode currentMode = Mode.NONE;
        private VocabEntryList entries = new VocabEntryList();
        private Map<String, String> rawEntry;
        private String currentId;

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) {
            currentMode = Mode.NONE;

            if (qName.equals(VOCAB_ENTRY_ELEMENT)) {
                rawEntry = new HashMap<String, String>();
                currentId = null;
                int idIdx = attributes.getIndex(VOCAB_ATTRIBUTE_ID);
                if (idIdx >= 0)
                    currentId = attributes.getValue(idIdx);
            } else if (qName.equals(VOCAB_LABEL_ELEMENT)) {
                currentMode = Mode.IN_FIELD_LABEL;
                fieldLabelBuf = new StringBuffer();
            } else if (qName.equals(VOCAB_VALUE_ELEMENT)) {
                fieldValueBuf = new StringBuffer();
                currentMode = Mode.IN_FIELD_VALUE;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals(VOCAB_VALUE_ELEMENT))
                rawEntry.put(fieldLabelBuf.toString().trim(), fieldValueBuf.toString().trim());
            else if (qName.equals(VOCAB_ENTRY_ELEMENT)) {
                VocabEntry entry;

                // 1. native id
                if (currentId != null)
                    entry = new VocabEntry(currentId);
                else if (rawEntry.containsKey(FieldFactory.ID))
                    entry = new VocabEntry(rawEntry.get(FieldFactory.ID));
                else
                    entry = new VocabEntry();

                entries.addEntry(entry);

                for (String rawName : rawEntry.keySet()) {
                    if (validFieldNames.contains(rawName))
                        entry.putFieldValue(rawName, rawEntry.get(rawName));
                    //else
                    //    System.out.println("Ignored vocab field " + rawName);
                }
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (currentMode == Mode.IN_FIELD_LABEL)
                fieldLabelBuf.append(ch, start, length);
            else if (currentMode == Mode.IN_FIELD_VALUE)
                fieldValueBuf.append(ch, start, length);
        }

        private enum Mode {
            NONE, IN_FIELD_LABEL, IN_FIELD_VALUE
        }
    }
}