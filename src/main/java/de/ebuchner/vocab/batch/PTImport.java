package de.ebuchner.vocab.batch;

import de.ebuchner.vocab.config.fields.FieldFactory;
import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;

import java.io.*;

public class PTImport {

    private static final File INPUT_FILE = new File("C:\\Users\\eb\\Desktop\\Mappe1.csv");
    private static final File OUTPUT_DIR = new File("C:\\_home\\eb\\bharat\\Language\\Portuguese\\vocab");
    private static final int MAX_VOCAB = 300;

    public static void main(String[] args) throws IOException {
        new PTImport().go();
    }

    private void go() throws IOException {
        cleanOutputDir();
        PTImportParser parser = new PTImportParser();
        parser.setListener(new PTImportHandler());
        parser.parse(INPUT_FILE);
    }

    private void cleanOutputDir() {
        File[] oldFiles = OUTPUT_DIR.listFiles();
        if (oldFiles == null)
            return;

        for (File oldFile : oldFiles) {
            if (oldFile.isDirectory())
                continue;
            if (!oldFile.delete())
                throw new RuntimeException("Could not delete " + oldFile);
        }
    }

    private static interface PTImportListener {

        void nextLine(String column1, String column2);

        void onFinished();
    }

    private static class PTImportHandler implements PTImportListener {
        VocabEntryList entryList = new VocabEntryList();
        VocabEntry currentEntry = null;
        int lessonCount = 0;
        int vocabCount = 0;

        private boolean empty(String s) {
            return s == null || s.trim().length() == 0;
        }

        public void nextLine(String foreign, String user) {
            System.out.printf("%s / %s%n", foreign, user);

            if (!empty(foreign) && !empty(user)) {
                vocabCount++;
                if (vocabCount >= MAX_VOCAB)
                    genFile();
                currentEntry = new VocabEntry();
                currentEntry.putFieldValue(FieldFactory.FOREIGN, foreign);
                entryList.addEntry(currentEntry);
            }

            if (!empty(foreign) && empty(user)) {
                addToComment(foreign);
            }

            if (!empty(user)) {
                if (currentEntry.getFieldValue(FieldFactory.USER) == null)
                    currentEntry.putFieldValue(FieldFactory.USER, user);
                else {
                    addToComment(user);
                }
            }
        }

        private void addToComment(String newComment) {
            String comment = currentEntry.getFieldValue(FieldFactory.COMMENT);
            if (comment != null) {
                comment = comment + System.getProperty("line.separator") + newComment;
            } else
                comment = newComment;
            currentEntry.putFieldValue(FieldFactory.COMMENT, comment);
        }

        private void genFile() {
            lessonCount++;
            File vocabFile = new File(
                    OUTPUT_DIR,
                    String.format("vocab-%02d.vocab", lessonCount)
            );

            VocabIOHelper.toFile(
                    vocabFile, entryList
            );
            System.out.println("Generated " + vocabFile.getName());
            entryList = new VocabEntryList();
            vocabCount = 0;
        }

        public void onFinished() {
            if (entryList.entryCount() > 0)
                genFile();
        }
    }

    private static class PTImportParser {
        PTImportListener listener;

        public void parse(File inputFile) throws IOException {

            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "iso-8859-1"));
            try {
                String line;
                int lineNo = 0;
                while ((line = r.readLine()) != null) {
                    parseLine(++lineNo, line);
                }

                listener.onFinished();
            } finally {
                r.close();
            }
        }

        private void parseLine(int lineNo, String line) {
            try {
                boolean quoted = false;
                int index = 0;
                final char separator = ';';
                final char quote = '"';
                StringBuilder token = new StringBuilder();

                String columns[] = new String[2];

                char[] lineBuffer = line.trim().toCharArray();
                for (int i = 0; i < lineBuffer.length; i++) {
                    if (quoted) {
                        if (lineBuffer[i] == quote) {
                            quoted = false;
                            columns[index++] = token.toString();
                            token = new StringBuilder();
                            for (; i < lineBuffer.length - 1 && lineBuffer[i] != separator; i++) ;
                        } else {
                            token.append(lineBuffer[i]);
                        }
                    } else {
                        if (lineBuffer[i] == quote) {
                            quoted = true;
                        } else if (lineBuffer[i] == separator) {
                            columns[index++] = token.toString();
                            token = new StringBuilder();
                        } else {
                            token.append(lineBuffer[i]);
                        }
                    }
                }
                if (token.length() > 0) {
                    columns[index] = token.toString();
                }
                listener.nextLine(columns[0], columns[1]);
            } catch (RuntimeException e) {
                throw new RuntimeException("Error at line " + lineNo, e);
            }
        }

        public void setListener(PTImportListener listener) {
            this.listener = listener;
        }
    }
}
