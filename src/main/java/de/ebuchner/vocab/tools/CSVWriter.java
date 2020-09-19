package de.ebuchner.vocab.tools;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CSVWriter {

    private static final String BOM = "\u00EF\u00BB\u00BF";

    public String csvSeparator(String locale) {
        if (locale == Locale.GERMAN.getLanguage())
            return ";";
        return ",";
    }

    public void toFile(File file, String separator, List<Map<String, String>> entries, List<String> fieldList) {
        try {
            if (file.exists())
                assert file.delete();
            // file.setText(BOM)

            PrintWriter printWriter =
                    new PrintWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(file, true), "UTF-8"
                            )
                    );
            try {
                printWriter.println(new OutputBuilder(
                        file,
                        separator,
                        entries,
                        fieldList
                ).toString());
            } finally {
                printWriter.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class OutputBuilder {
        String separator;
        File file;
        List<Map<String, String>> entries;
        List<String> fieldList;
        StringBuilder output = new StringBuilder();

        public OutputBuilder(File file, String separator, List<Map<String, String>> entries, List<String> fieldList) {
            this.file = file;
            this.separator = separator;
            this.entries = entries;
            this.fieldList = fieldList;
        }

        public String toString() {
            appendHeader();

            for (Map<String, String> entry : entries) {
                appendLine(entry);
            }

            return output.toString();
        }

        private void appendHeader() {
            for (String fieldName : fieldList) {
                appendCell(fieldName);
            }
        }

        private void appendLine(Map<String, String> entry) {
            output.append(System.getProperty("line.separator"));
            for (String field : fieldList) {
                appendCell(entry.get(field));
            }
        }

        private void appendCell(String content) {
            if (content != null) {
                content.replaceAll(" \"", "\" \"");
                if (content.indexOf('\n') >= 0 ||
                        content.indexOf('\r') >= 0 ||
                        content.indexOf(' ') >= 0 ||
                        content.indexOf(separator) >= 0
                        )
                    output.append(content);
                else
                    output.append(content);
            }
            output.append(separator);
        }
    }
}

