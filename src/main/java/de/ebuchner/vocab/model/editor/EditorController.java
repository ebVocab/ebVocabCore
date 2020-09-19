package de.ebuchner.vocab.model.editor;

import de.ebuchner.vocab.config.ConfigConstants;
import de.ebuchner.vocab.model.VocabBaseController;
import de.ebuchner.vocab.model.lessons.LessonModel;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.lessons.entry.VocabEntryRef;
import de.ebuchner.vocab.model.nui.NuiWindowParameter;
import de.ebuchner.vocab.model.nui.WindowType;
import de.ebuchner.vocab.model.nui.platform.UIPlatformFactory;

import java.io.File;
import java.util.List;

public class EditorController extends VocabBaseController {

    private EditorModel editorModel;
    private EditorFileModelBehaviour fileModel;
    private EditorWindowBehaviour editorWindow;
    private EntrySelection entrySelection;
    private EntryInEdit entryInEdit;
    private boolean inTableSelectionChanged = false;

    public EditorController(EditorWindowBehaviour editorWindow, EntryInEditWindowBehaviour entryInEditWindow) {
        this.editorWindow = editorWindow;
        this.entrySelection = new EntrySelection(editorWindow.decorateModel(new EditorFileModel()));
        this.fileModel = entrySelection;
        this.entryInEdit = new EntryInEdit(fileModel, entryInEditWindow);
        this.editorModel = EditorModel.getOrCreateEditorModel();
    }

    public static void editVocabEntry(VocabEntryRef entryRef) {
        EditorWindowParameter parameter =
                new EditorWindowParameter(EditorWindowParameter.Action.OPEN, entryRef);
        EditorModel editorModel = EditorModel.getOrCreateEditorModel();
        if (editorModel.lock().isLocked(
                parameter.getVocabEntryRef().getFileRef()
        ))
            UIPlatformFactory.getUIPlatform().getNuiDirector().sendWindow(parameter);
        else
            UIPlatformFactory.getUIPlatform().getNuiDirector().showWindow(WindowType.EDITOR_WINDOW, parameter);
    }

    public void onWindowWasCreated() {
        updateEditorWindow();
    }

    public void onEditorStart(EditorWindowParameter parameter) {
        if (parameter.getVocabEntryRef() == null)
            return;

        VocabEntryRef currentRef = parameter.getVocabEntryRef();
        if (editorModel.lock().isLocked(currentRef.getFileRef()))
            return;

        fileOpenImpl(currentRef.getFileRef());
        startEditing(currentRef);
    }

    private boolean startEditing(VocabEntryRef ref) {
        if (!entryInEdit.acceptAndLeaveEditingMode())
            return false;

        entrySelection.clearSelection();
        VocabEntry entryToSelect = fileModel.findEntry(ref.getId());
        if (entryToSelect == null)
            return false;

        entrySelection.addSelection(entryToSelect);
        entryInEdit.startEditingExistingEntry(entryToSelect);

        updateEditorWindow();

        return true;
    }

    public CheckResult onWindowClosing() {
        return checkModified();
    }

    public void onWindowClosed() {
        if (fileModel.hasFile())
            editorModel.lock().unLockFile(fileModel.getFile());
    }

    public void onEntryAdd() {
        if (!entryInEdit.acceptAndLeaveEditingMode())
            return;
        entryInEdit.startEditingNewEntry();

        updateEditorWindow();
    }

    public void onEntriesDelete() {
        List<VocabEntry> entriesToDelete = entrySelection.allEntries();

        // changes selection
        if (!entryInEdit.acceptAndLeaveEditingMode())
            return;

        fileModel.deleteEntries(entriesToDelete);
        if (entrySelection.selectionCount() == 1)
            entryInEdit.startEditingExistingEntry(entrySelection.allEntries().get(0));

        updateEditorWindow();
    }

    public void onEntryMove(EditorFileModel.MoveEntryDirection direction) {
        List<VocabEntry> entriesToMove = entrySelection.allEntries();
        if (entriesToMove.size() != 1)
            return;

        // changes selection
        if (!entryInEdit.acceptAndLeaveEditingMode())
            return;

        VocabEntry entryToMove = entriesToMove.get(0);

        fileModel.moveEntry(entryToMove, direction);

        entrySelection.clearSelection();
        entrySelection.addSelection(entryToMove);
        entryInEdit.startEditingExistingEntry(entryToMove);

        updateEditorWindow();
    }

    public void onEntryRevert() {
        entryInEdit.revert();
        updateEditorWindow();
    }

    public void onEntryAccept() {
        entryInEdit.acceptAndLeaveEditingMode();
        updateEditorWindow();
    }

    public void onTableSelectionChanged(List<VocabEntry> vocabEntries) {
        if (inTableSelectionChanged)
            return;

        inTableSelectionChanged = true;
        try {
            if (!entryInEdit.acceptAndLeaveEditingMode())
                return;

            entrySelection.updateSelectedEntries(vocabEntries);

            switch (vocabEntries.size()) {
                case 0:
                    editorWindow.removeSingleRowSelectionAndFireNoEvents();
                    break;
                case 1:
                    entryInEdit.startEditingExistingEntry(vocabEntries.get(0));
                    break;
            }

            updateEditorWindow();
        } finally {
            inTableSelectionChanged = false;
        }
    }

    public void onFileNew() {
        if (checkModified() == CheckResult.CANCEL)
            return;

        fileNewImpl();
    }

    private void updateEditorWindow() {
        editorWindow.setWindowTitle(fileModel.getFile());
        editorWindow.setEntrySelection(entrySelection);
        editorWindow.setSaveOptionEnabled(fileModel.isDirty() && fileModel.entryCount() > 0);
        editorWindow.setSaveAsOptionEnabled(fileModel.entryCount() > 0);
        if (entryInEdit.isActionNone())
            editorWindow.removeSingleRowSelectionAndFireNoEvents();
    }

    public boolean onFileOpen() {
        if (checkModified() == CheckResult.CANCEL)
            return false;
        File fileToOpen = editorWindow.openFileDialog(
                editorModel.getPreferredDirectory(),
                ConfigConstants.FILE_EXTENSION
        );
        if (fileToOpen == null)
            return false;

        return onFileOpen2ndStep(fileToOpen);
    }

    /**
     * A mobile app cannot immediately reply when openFileDialog() is called because asynchronous
     * activity switching is necessary to determine a file name. So it will
     * trigger file open activity when openFileDialog() is called and then calls this 2nd step
     * of the file open process when it receives a file name
     */
    public boolean onFileOpen2ndStep(File fileToOpen) {
        return fileOpenImpl(fileToOpen);
    }

    public boolean onFileSave() {
        if (!entryInEdit.acceptAndLeaveEditingMode())
            return false;

        if (fileModel.isEmpty()) {
            editorWindow.sendMessageEmptyFile();
            return false;
        }

        if (fileModel.hasFile())
            return fileSaveImpl(fileModel.getFile());

        File file = editorWindow.saveFileDialog(
                editorModel.getPreferredDirectory(),
                ConfigConstants.FILE_EXTENSION
        );
        if (file == null)
            return false;

        return fileSaveAsImpl(file);
    }

    public boolean onFileSaveAs() {
        File file = editorWindow.saveFileDialog(
                editorModel.getPreferredDirectory(),
                ConfigConstants.FILE_EXTENSION
        );
        if (file == null)
            return false;

        return fileSaveAsImpl(file);
    }

    public void onFileRevert() {
        if (!editorWindow.askToRevert())
            return;
        onFileRevert2ndStep();
    }

    public void onFileRevert2ndStep() {
        File currentFile = fileModel.getFile();
        if (!updateEditorModel(null))
            return;
        fileOpenImpl(currentFile);
    }

    protected CheckResult checkModified() {
        if (!entryInEdit.acceptAndLeaveEditingMode())
            return CheckResult.CANCEL;

        if (!fileModel.isDirty())
            return CheckResult.CONTINUE;

        return checkModified2ndStep(editorWindow.askToSave());
    }

    public CheckResult checkModified2ndStep(EditorWindowBehaviour.AskToSaveResult result) {
        if (result == EditorWindowBehaviour.AskToSaveResult.DO_CANCEL)
            return CheckResult.CANCEL;
        else if (result == EditorWindowBehaviour.AskToSaveResult.DO_IGNORE)
            return CheckResult.CONTINUE;

        if (fileModel.hasFile()) {
            if (!fileSaveImpl(fileModel.getFile()))
                return CheckResult.CANCEL;
            return CheckResult.CONTINUE;
        }

        File file = editorWindow.saveFileDialog(
                editorModel.getPreferredDirectory(),
                ConfigConstants.FILE_EXTENSION
        );
        if (file == null)
            return CheckResult.CANCEL;

        return fileSaveAsImpl(file) ? CheckResult.CONTINUE : CheckResult.CANCEL;
    }

    private boolean fileNewImpl() {
        if (!updateEditorModel(null))
            return false;

        fileModel.clearFile();

        updateEditorWindow();

        return true;
    }

    private boolean fileOpenImpl(File file) {
        if (!updateEditorModel(file))
            return false;

        fileModel.openFile(file);

        updateEditorWindow();

        return true;
    }

    /**
     * @see #onFileOpen2ndStep(File)
     */
    public boolean onFileSave2ndStep(File file) {
        // can only be "save as", otherwise the editor ui would not be involved (and this is the
        // callback for the editor ui) In case of "save" the file name is already well known
        return fileSaveAsImpl(file);
    }

    private boolean fileSaveAsImpl(File file) {
        if (!updateEditorModel(file))
            return false;

        return fileSaveImpl(file);
    }

    private boolean fileSaveImpl(File file) {
        fileModel.saveFile(file);
        editorModel.fireModelChanged();
        updateEditorWindow();

        return true;
    }

    private boolean updateEditorModel(File file) {
        if (file == null) {
            if (fileModel.hasFile())
                editorModel.lock().unLockFile(fileModel.getFile());
            return true;
        }

        if (editorModel.lock().isLocked(file)) {
            editorWindow.sendMessageFileLockFailed(file);
            return false;
        }

        if (fileModel.hasFile()) {
            editorModel.lock().unLockFile(fileModel.getFile());
        }

        editorModel.lock().lockFile(file);
        editorModel.setPreferredDirectory(file.getParentFile());

        return true;
    }

    public boolean handleWindowParameter(NuiWindowParameter nuiWindowParameter) {
        if (nuiWindowParameter == null)
            throw new NullPointerException("window parameter may not be null");

        if (nuiWindowParameter == NuiWindowParameter.EMPTY)
            nuiWindowParameter = new EditorWindowParameter(EditorWindowParameter.Action.NEW);
        else if (!(nuiWindowParameter instanceof EditorWindowParameter))
            throw new IllegalArgumentException("window parameter has wrong type " + nuiWindowParameter.getClass());

        EditorWindowParameter parameter = (EditorWindowParameter) nuiWindowParameter;
        if (parameter.getAction() == EditorWindowParameter.Action.NEW)
            return true;

        if (parameter.getVocabEntryRef() == null && parameter.getAction() == EditorWindowParameter.Action.OPEN) {
            LessonModel lessonModel = LessonModel.getOrCreateLessonModel();
            if (lessonModel.countAvailableLessons() > 0)
                return onFileOpen();
            return true;
        }

        onEditorStart(parameter);
        return true;
    }

    EditorWindowParameter toEditorWindowParameter(NuiWindowParameter nuiWindowParameter) {
        if (nuiWindowParameter == null || !(nuiWindowParameter instanceof EditorWindowParameter))
            return null;
        return (EditorWindowParameter) nuiWindowParameter;
    }

    public void onReceive(NuiWindowParameter nuiWindowParameter) {
        EditorWindowParameter parameter = toEditorWindowParameter(nuiWindowParameter);
        if (parameter == null)
            return;

        if (parameter.getVocabEntryRef() == null)
            return;
        if (parameter.getVocabEntryRef().getFileRef() == null)
            return;
        if (!parameter.getVocabEntryRef().getFileRef().equals(fileModel.getFile()))
            return;

        startEditing(parameter.getVocabEntryRef());
    }

    public boolean onCanCreateWindow(NuiWindowParameter nuiWindowParameter) {
        EditorWindowParameter parameter = toEditorWindowParameter(nuiWindowParameter);
        if (parameter == null)
            return true;
        if (parameter.getVocabEntryRef() == null)
            return true;
        if (parameter.getVocabEntryRef().getFileRef() == null)
            return true;

        File file = parameter.getVocabEntryRef().getFileRef();
        boolean canCreate = !editorModel.lock().isLocked(file);
        if (!canCreate)
            editorWindow.sendMessageFileLockFailed(file);
        return canCreate;
    }

    public void onClipboardCopy() {
        List<VocabEntry> entriesToCopy = entrySelection.allEntries();
        UIPlatformFactory.getUIPlatform().getNuiDirector().entriesToClipboard(entriesToCopy);
    }

    public void onClipboardCut() {
        onClipboardCopy();                                                                                         
        onEntriesDelete();
    }

    public void onClipboardPaste() {
        List<VocabEntry> entries = UIPlatformFactory.getUIPlatform().getNuiDirector().entriesFromClipboard();
        if (entries == null) {
            editorWindow.sendMessageClipboardEmpty();
            return;
        }
        for (VocabEntry entry : entries) {
            entry.calculateAnotherId();
            fileModel.addEntry(entry);
        }
    }

    public enum CheckResult {
        CANCEL, CONTINUE
    }


}
