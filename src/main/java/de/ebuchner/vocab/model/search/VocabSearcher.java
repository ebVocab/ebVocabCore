package de.ebuchner.vocab.model.search;

import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.model.lessons.Lesson;
import de.ebuchner.vocab.model.lessons.LessonContainer;
import de.ebuchner.vocab.model.lessons.LessonModel;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryList;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VocabSearcher {

    private VocabSearchOptions searchOptions;

    public VocabSearcher(VocabSearchOptions searchOptions) {
        this.searchOptions = searchOptions;
    }

    public Result doSearch(String searchExpression) {
        Result result = new Result();

        LessonModel model = new LessonModel();
        LessonContainer container = model.getRoot();

        SearchOptions options = new SearchOptions();
        options.setCaseSensitive(searchOptions.isCaseSensitive());
        options.setWholeWords(searchOptions.isWholeWords());
        options.setRegularExpression(searchOptions.isRegularExpression());

        TextSearcher searcher = new TextSearcher(options, searchExpression);

        doSearch(container, searcher, result);

        return result;
    }

    private void doSearch(LessonContainer container, TextSearcher searcher, Result result) {
        for (Lesson lesson : container.lessons()) {
            if (lesson.isContainer()) {
                doSearch(lesson.asContainer(), searcher, result);
                continue;
            }
            File file = lesson.getFileRef();
            if(VocabIOHelper.isRefFile(file))
                continue;

            VocabEntryList entryList = VocabIOHelper.fromFile(file);
            for (VocabEntry entry : entryList.entries()) {
                for (String fieldName : entry.fieldNames()) {
                    if (!searchOptions.isSearchField(fieldName))
                        continue;

                    String fieldValue = entry.getFieldValue(fieldName);

                    if (searcher.matches(fieldValue)) {
                        result.addEntry(lesson.getFileRef(), entry);
                        break;
                    }
                }
            }
        }
    }

    public static class Result {
        private List<VocabEntry> entries = new ArrayList<>();
        private List<VocabEntryRef> entryRefs = new ArrayList<>();

        void addEntry(File fileRef, VocabEntry entry) {
            entryRefs.add(VocabEntryRef.fromEntry(fileRef, entry));
            entries.add(entry);
        }

        public List<VocabEntry> getEntries() {
            return entries;
        }

        public List<VocabEntryRef> getEntryRefs() {
            return entryRefs;
        }

        public int size() {
            return entries.size();
        }

        public VocabEntry getEntry(VocabEntryRef entryRef) {
            for (VocabEntry entry : entries) {
                if (entry.getId().equals(entryRef.getId()))
                    return entry;
            }
            return null;
        }
    }

}
