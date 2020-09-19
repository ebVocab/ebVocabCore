package de.ebuchner.vocab.model.editor;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.preferences.PreferenceValueList;
import de.ebuchner.vocab.config.preferences.StringValue;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.core.AbstractModel;
import de.ebuchner.vocab.model.core.ModelChangeEvent;

import java.io.File;
import java.io.IOException;

public class EditorModel extends AbstractModel<EditorCommand, EditorModelListener> {
    private static final String PREFERRED_DIRECTORY = "preferred.directory";
    private long lastSaved = 0L;
    private FileLocker fileLocker = new FileLocker();
    private File preferredDirectory;

    public static EditorModel getOrCreateEditorModel() {
        return (EditorModel) VocabModel.getInstance().getOrCreateModel(EditorModel.class);
    }

    @Override
    protected void fireModelChanged() {
        ModelChangeEvent event = new ModelChangeEvent(this);
        for (EditorModelListener listener : listeners) {
            listener.editorChanged(event);
        }
    }

    @Override
    public void reSynchronize() {
    }

    public void restoreFromPreferences(PreferenceValueList preferenceValues) {
        StringValue directoryName = (StringValue) preferenceValues.getName(EditorModel.class, PREFERRED_DIRECTORY);
        if (directoryName != null)
            preferredDirectory = new File(directoryName.asString());
        else
            preferredDirectory = Config.instance().getProjectInfo().getVocabDirectory();
    }

    public void saveToPreferences(PreferenceValueList preferenceValues) {
        if (preferredDirectory == null)
            return;
        String directoryName;
        try {
            directoryName = preferredDirectory.getCanonicalPath();
        } catch (IOException e) {
            directoryName = preferredDirectory.getAbsolutePath();
        }
        preferenceValues.putName(
                EditorModel.class,
                PREFERRED_DIRECTORY,
                new StringValue(directoryName));
    }

    public FileLocker lock() {
        return fileLocker;
    }

    public File getPreferredDirectory() {
        return preferredDirectory;
    }

    public void setPreferredDirectory(File preferredDirectory) {
        this.preferredDirectory = preferredDirectory;
    }

    public void fileSaved() {
        lastSaved = System.currentTimeMillis();
    }

    public long getLastSaved() {
        return lastSaved;
    }

}
