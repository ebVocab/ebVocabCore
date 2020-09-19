package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.config.fields.FieldFactory;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FieldEncoder implements VocabIOConstants {

    public void encodeZip(ZipOutputStream outputStream, VocabEntryList entries) {
        ZipEntry zipEntry = new ZipEntry(VOCAB_PART_PREFIX + VOCAB_PART_SUFFIX);
        try {
            outputStream.putNextEntry(zipEntry);

            encode(outputStream, entries);

            outputStream.closeEntry();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void encode(OutputStream outputStream, VocabEntryList entries) {
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = documentBuilder.newDocument();
            Element rootNode = document.createElement(VOCAB_ROOT_NODE);
            document.appendChild(rootNode);

            fieldsToDom(document, rootNode, entries.entries());

            t.transform(new DOMSource(rootNode), new StreamResult(outputStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encodeToString(List<VocabEntry> entries) {
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = documentBuilder.newDocument();
            Element rootNode = document.createElement(VOCAB_ROOT_NODE);
            document.appendChild(rootNode);

            fieldsToDom(document, rootNode, entries);

            StringWriter writer = new StringWriter();
            t.transform(new DOMSource(rootNode), new StreamResult(writer));

            return writer.getBuffer().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void fieldsToDom(Document document, Element rootNode, Iterable<VocabEntry> entries) {
        for (VocabEntry entry : entries) {
            Element entryNode = document.createElement(VOCAB_ENTRY_ELEMENT);
            rootNode.appendChild(entryNode);

            entryNode.setAttribute(VOCAB_ATTRIBUTE_ID, entry.getId());

            for (String fieldName : entry.fieldNames()) {
                if (FieldFactory.getGenericField(fieldName) == null) {
                    System.out.println("Ignoring field " + fieldName);
                    continue;
                }
                String fieldValue = entry.getFieldValue(fieldName);
                addField(document, entryNode, fieldName, fieldValue);
            }
        }
    }

    private void addField(Document document, Element entryNode, String fieldName, String fieldValue) {
        Element fieldNode = document.createElement(VOCAB_FIELD_ELEMENT);
        entryNode.appendChild(fieldNode);

        Element labelNode = document.createElement(VOCAB_LABEL_ELEMENT);
        labelNode.appendChild(document.createTextNode(fieldName));
        fieldNode.appendChild(labelNode);

        Element valueNode = document.createElement(VOCAB_VALUE_ELEMENT);
        valueNode.appendChild(document.createTextNode(fieldValue));
        fieldNode.appendChild(valueNode);
    }

}
