package de.ebuchner.vocab.batch;

import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;
import de.ebuchner.vocab.model.project.ProjectConfiguration;
import de.ebuchner.vocab.tools.URLTools;

import java.io.File;
import java.net.MalformedURLException;

public class AlphabetVocabFileConverter {

    private AlphabetVocabFileConverter() {

    }

    public static void main(String args[]) throws MalformedURLException {
        File vocabFile = null;
        File projectDir = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-vocabFile"))
                vocabFile = new File(args[i + 1]);
            else if (args[i].equals("-projectDir"))
                projectDir = new File(args[i + 1]);
        }

        if (vocabFile == null || !vocabFile.exists() || vocabFile.isDirectory())
            throw new IllegalArgumentException("Missing or invalid parameter -vocabFile");
        if (projectDir == null || !projectDir.exists() || projectDir.isFile())
            throw new IllegalArgumentException("Missing or invalid parameter -projectDir");

        ProjectConfiguration.startupWithProjectDirectory(projectDir);
        VocabEntryList alphabetEntries = VocabIOHelper.fromText(
                URLTools.asText(
                        vocabFile.toURI().toURL()
                )
        );
        VocabIOHelper.toFile(vocabFile, alphabetEntries);
    }
}
