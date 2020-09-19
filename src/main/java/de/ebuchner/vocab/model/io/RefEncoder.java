package de.ebuchner.vocab.model.io;

import de.ebuchner.vocab.config.ProjectConfig;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.OutputStream;
import java.util.List;

public class RefEncoder implements VocabIOConstants {

    public void encode(OutputStream outputStream, List<VocabEntryRef> entryRefs) {
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = documentBuilder.newDocument();
            Element rootNode = document.createElement(VOCAB_REF_ROOT_NODE);
            document.appendChild(rootNode);

            for (VocabEntryRef entryRef : entryRefs) {
                Element refNode = document.createElement(VOCAB_REF_ELEMENT);
                refNode.setAttribute(VOCAB_REF_FILE_ELEMENT, relativePathToVocabDir(entryRef.getFileRef()));   
                refNode.setAttribute(VOCAB_ATTRIBUTE_ID, entryRef.getId());
                rootNode.appendChild(refNode);
            }

            t.transform(new DOMSource(rootNode), new StreamResult(outputStream));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String relativePathToVocabDir(File vocabFile) {
        File vocabDir = ProjectConfig.instance().getProjectInfo().getVocabDirectory();
        return vocabDir.toURI().relativize(vocabFile.toURI()).getPath().replace("\\", "/");
    }

}
