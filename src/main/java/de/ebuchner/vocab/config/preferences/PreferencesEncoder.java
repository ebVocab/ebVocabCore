package de.ebuchner.vocab.config.preferences;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;

/*public*/ class PreferencesEncoder implements PreferencesIOConstants {

    public void encode(OutputStream outputStream, PreferenceValueList values) {
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = documentBuilder.newDocument();
            Element rootNode = document.createElement(ELEMENT_ROOT);
            document.appendChild(rootNode);

            valuesToDom(document, rootNode, values);

            t.transform(new DOMSource(rootNode), new StreamResult(outputStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void valuesToDom(Document document, Element rootNode, PreferenceValueList values) {

        for (String preferenceName : values.names()) {
            Element entry = document.createElement(ELEMENT_ENTRY);
            rootNode.appendChild(entry);

            PreferenceValue preferenceValue = values.getName(preferenceName);

            Element className = document.createElement(ELEMENT_CLASS_NAME);
            entry.appendChild(className);
            className.setTextContent(XmlUtils.toXml(preferenceValue.getClass().getName()));

            Element nameElement = document.createElement(ELEMENT_PREFERENCE_NAME);
            entry.appendChild(nameElement);
            nameElement.setTextContent(XmlUtils.toXml(preferenceName));

            Element valueElement = document.createElement(ELEMENT_PREFERENCE_VALUE);
            entry.appendChild(valueElement);
            valueElement.setTextContent(XmlUtils.toXml(preferenceValue.asString()));
        }

    }
}
