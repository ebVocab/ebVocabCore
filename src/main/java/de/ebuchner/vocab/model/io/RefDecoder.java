package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.config.ProjectConfig;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;
import de.ebuchner.vocab.tools.StreamBuffer;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RefDecoder implements VocabIOConstants, IVocabDecoder {

    private final InputStream inputStream;

    public RefDecoder(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<VocabEntryRef> decode() {
        RefEncoderXmlHandler xmlHandler = new RefEncoderXmlHandler();

        try {
            // SAXParser closes automatically the input stream -> create duplicate
            InputStream inputStreamDecorator = new StreamBuffer(inputStream).duplicateInputStream();

            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(inputStreamDecorator, xmlHandler);

            return xmlHandler.entryRefs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public VocabEntryList decodeEntries() {
        VocabEntryList resultList = new VocabEntryList();

        for (VocabEntryRef entryRef : decode()) {
            VocabEntryList entryList = VocabIOHelper.fromFile(entryRef.getFileRef());
            for (VocabEntry entry : entryList.entries()) {
                if (entry.getId().equals(entryRef.getId())) {
                    resultList.addEntry(entry);
                }
            }
        }
        return resultList;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    static class RefEncoderXmlHandler extends DefaultHandler {

        List<VocabEntryRef> entryRefs = new ArrayList<>();

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) {
            if (qName.equals(VOCAB_REF_ELEMENT)) {
                String id = attributes.getValue(VOCAB_ATTRIBUTE_ID);
                String fileRef = attributes.getValue(VOCAB_REF_FILE_ELEMENT);

                File vocabDir = ProjectConfig.instance().getProjectInfo().getVocabDirectory();
                VocabEntryRef vocabEntryRef = new VocabEntryRef(new File(vocabDir, fileRef), id);
                vocabEntryRef.setFileRefString(fileRef);
                entryRefs.add(vocabEntryRef);
            }
        }
    }
}
