package de.ebuchner.vocab.model.cloud;

import de.ebuchner.vocab.config.ProjectInfo;
import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.model.io.VocabTimeStamp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FileList extends CloudList {
    private List<FileItem> fileItems = new ArrayList<FileItem>();

    private final String slash = "/";

    public FileList() {
    }

    public FileList(ProjectInfo projectInfo) {
        fromDir(projectInfo.getVocabDirectory(), slash);
    }

    private void fromDir(File vocabDir, String relativePath) {
        File[] vocabDirEntries = vocabDir.listFiles();
        if (vocabDirEntries == null)
            return;

        for (File entry : vocabDirEntries) {
            if (entry.isDirectory()) {
                String subDirPath = relativePath + entry.getName() + slash;
                fromDir(entry, subDirPath);
            } else {
                VocabTimeStamp timeStamp = VocabIOHelper.timeStampOf(entry);
                FileItem item = new FileItem();
                item.setFileName(entry.getName());
                item.setLastModified(timeStamp.getTimeStamp());
                item.setRelativePath(relativePath);
                fileItems.add(item);
            }
        }
    }

    public FileList(String cloudServerResponse) {
        StringTokenizer lines = new StringTokenizer(cloudServerResponse, "\n");
        while (lines.hasMoreElements()) {
            String line = lines.nextToken();

            StringTokenizer fields = new StringTokenizer(line, ";");
            FileItem fileItem = new FileItem();
            fileItems.add(fileItem);
            for (int i = 0; fields.hasMoreTokens(); i++) {
                String field = fields.nextToken();
                switch (i) {
                    case 0:
                        fileItem.setRelativePath(extract(field));
                        break;
                    case 1:
                        fileItem.setFileName(extract(field));
                        break;
                    case 2:
                        // locale
                        break;
                    case 3:
                        fileItem.setLastModified(Long.parseLong(field));
                        break;
                }
            }

        }
    }

    public int size() {
        return fileItems.size();
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }

    public FileItem findByTemplate(FileItem template) {
        for (FileItem item : fileItems) {
            if (item.getFileName().equals(template.getFileName()) &&
                    item.getRelativePath().equals(template.getRelativePath()))
                return item;
        }
        return null;
    }

}
