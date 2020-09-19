package de.ebuchner.vocab.config;

import de.ebuchner.vocab.config.fields.Field;
import de.ebuchner.vocab.config.fields.FieldFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Config implements ConfigBehaviour {

    private static final String AUTO_SAVE_DATE_SUFFIX = "yyyy.MM.dd_HHmmss";
    private static Config systemConfig = new SystemConfig();
    private static Config projectConfig = null;

    private List<Field> fieldList = null;

    public static Config instance() {
        return projectConfig != null ? projectConfig : systemConfig;
    }

    public static void changeProjectConfig(ProjectConfig projectConfig) {
        if (Config.projectConfig != null)
            throw new IllegalStateException("Project config cannot be initialized twice");
        Config.projectConfig = projectConfig;
    }

    public static boolean projectInitialized() {
        return projectConfig != null;
    }

    @Override
    public final String getSystemLocale() {
        String sourceLocale = System.getProperty("vocab.keyboard.source.locale");
        if (sourceLocale == null)
            sourceLocale = Locale.getDefault().getLanguage();
        return sourceLocale;
    }

    public final List<Field> fieldList() {
        if (fieldList != null)
            return fieldList;

        fieldList = new ArrayList<Field>();
        for (Field field : FieldFactory.getGenericFieldList()) {
            if (field.active())
                fieldList.add(field);
        }
        return fieldList;
    }

    @Override
    public final List<Field> fieldListEditable() {
        List<Field> editableFields = new ArrayList<Field>();
        for (Field field : fieldList()) {
            if (field.editable())
                editableFields.add(field);
        }
        return editableFields;
    }

    @Override
    public final List<Field> fieldListVisible() {
        List<Field> visibleFields = new ArrayList<Field>();
        for (Field field : fieldList()) {
            if (!field.internal())
                visibleFields.add(field);
        }
        return visibleFields;
    }

    /**
     * if you overwrite this method call super.dispose() before your method exits
     */
    @Override
    public void dispose() {
        projectConfig = null;
    }

    public File autoSaveFile(String suffix, String extension) {
        File autoSaveDirectory = getProjectInfo().getAutoSaveDirectory();
        if (!autoSaveDirectory.exists()) {
            if (!autoSaveDirectory.mkdirs())
                throw new RuntimeException("Could not create directory " + autoSaveDirectory.getAbsolutePath());
        }

        Calendar now = new GregorianCalendar();
        String datePrefix = new SimpleDateFormat(AUTO_SAVE_DATE_SUFFIX, Locale.ENGLISH).format(now.getTime());

        return new File(
                autoSaveDirectory,
                String.format(
                        "%s_%s.%s",
                        datePrefix,
                        suffix,
                        extension
                )
        );
    }

    public Iterable<? extends File> listAutoSavedFiles(String suffix, String extension) {
        final String suffixWithExt = String.format("_%s.%s", suffix, extension);
        File autoSaveDirectory = getProjectInfo().getAutoSaveDirectory();
        File[] files = autoSaveDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(suffixWithExt);
            }
        });
        if (files == null || files.length == 0)
            return Collections.emptyList();

        TreeSet<File> sortedFileList = new TreeSet<>(new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        Collections.addAll(sortedFileList, files);

        return sortedFileList;
    }

    public Date parseAutoSaveDateTime(File repetitionFile) throws ParseException {
        String fileName = repetitionFile.getName();
        if (fileName.length() < AUTO_SAVE_DATE_SUFFIX.length())
            return null;
        return new SimpleDateFormat(AUTO_SAVE_DATE_SUFFIX, Locale.ENGLISH)
                .parse(fileName.substring(0, AUTO_SAVE_DATE_SUFFIX.length()));
    }
}
