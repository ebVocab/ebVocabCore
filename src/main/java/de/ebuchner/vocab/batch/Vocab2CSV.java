package de.ebuchner.vocab.batch;

import de.ebuchner.vocab.config.fields.FieldFactory;
import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;

import java.io.*;

public class Vocab2CSV {

    public static void main(String[] args) throws IOException {
        File input = null;
        File output = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-input"))
                input = new File(args[++i]);
            if (args[i].equals("-output"))
                output = new File(args[++i]);
        }

        if (input == null) {
            System.err.println("missing parameter -input");
            System.exit(1);
        }

        if (output == null)
            output = new File(input.getAbsolutePath() + ".csv");

        new Vocab2CSV().convert2CSV(input, output);
    }

    private void convert2CSV(File input, File output) throws IOException {
        VocabEntryList entryList = VocabIOHelper.fromFile(input);

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8"))) {
            writer.println("Foreign; Type; User; Comment");
            for (VocabEntry entry : entryList.entries()) {
                writer.printf("%s;", outputField(entry.getFieldValue(FieldFactory.FOREIGN)));
                writer.printf("%s;", outputField(entry.getFieldValue(FieldFactory.TYPE)));
                writer.printf("%s;", outputField(entry.getFieldValue(FieldFactory.USER)));
                writer.printf("%s", outputField(entry.getFieldValue(FieldFactory.COMMENT)));
                writer.println();
            }
        }
    }

    private String outputField(String fieldValue) {
        if (fieldValue == null)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\"");

        String value = fieldValue.replaceAll("\"", "\"\"");

        stringBuilder.append(value).append("\"");
        return stringBuilder.toString();

    }
}
