package de.ebuchner.vocab.nui.common;

import de.ebuchner.toolbox.i18n.I18NContext;
import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.fields.FieldFactory;
import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class RepetitionItem {

    private final I18NContext i18n = I18NLocator.locate();
    private final File repetitionFile;
    private final String itemString;

    public RepetitionItem(File repetitionFile) throws ParseException, IOException {
        this.repetitionFile = repetitionFile;
        this.itemString = initItemString(repetitionFile);
    }

    private String initItemString(File repetitionFile) throws ParseException, IOException {
        Date date = Config.instance().parseAutoSaveDateTime(repetitionFile);
        StringBuilder itemStringBuilder = new StringBuilder();
        itemStringBuilder.append(
                DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                        .format(date)
        );
        itemStringBuilder.append(" ");

        List<VocabEntryRef> refList = VocabIOHelper.fromRefFile(repetitionFile);
        if (refList == null || refList.size() == 0)
            itemStringBuilder.append(i18n.getString("nui.repetition.invalid.file"));
        else {
            for (VocabEntryRef entryRef : refList) {
                VocabEntryList entryList = VocabIOHelper.fromFile(entryRef.getFileRef());
                for (VocabEntry entry : entryList.entries()) {
                    if (entry.getId().equals(entryRef.getId())) {
                        itemStringBuilder.append(entry.getFieldValue(FieldFactory.FOREIGN));
                        itemStringBuilder.append("; ");
                    }
                }
            }
        }
        return itemStringBuilder.toString();
    }

    public File getRepetitionFile() {
        return repetitionFile;
    }

    @Override
    public String toString() {
        return itemString;
    }
}
