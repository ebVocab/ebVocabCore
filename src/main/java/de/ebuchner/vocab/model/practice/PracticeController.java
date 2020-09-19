package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.ConfigConstants;
import de.ebuchner.vocab.model.VocabBaseController;
import de.ebuchner.vocab.model.core.ModelChangeEvent;
import de.ebuchner.vocab.model.core.ModelCommandManagerClearedEvent;
import de.ebuchner.vocab.model.editor.EditorController;
import de.ebuchner.vocab.model.editor.EditorModel;
import de.ebuchner.vocab.model.editor.EditorModelListener;
import de.ebuchner.vocab.model.io.VocabIOHelper;
import de.ebuchner.vocab.model.lessons.*;
import de.ebuchner.vocab.model.nui.NuiWindow;
import de.ebuchner.vocab.model.nui.WindowType;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PracticeController extends VocabBaseController
        implements PracticeModelListener, LessonSelectionChangeListener,
        PracticeStateHolder, StrategyChangeListener, VocabEntryModelListener, EditorModelListener {
    private static final Logger LOGGER = Logger.getLogger(PracticeController.class.getName());
    protected PracticeModel practiceModel;
    private PracticeWindowBehaviour practiceWindow;
    private LessonModel lessonModel;
    private PracticeState practiceState;
    private AbstractPracticeStrategy practiceStrategy;
    private VocabEntryModel vocabEntryModel;
    private boolean deaf = false;

    public PracticeController(PracticeWindowBehaviour practiceWindow) {
        this.practiceWindow = practiceWindow;

        this.practiceState = new PracticeStateDisabled(this);
        this.practiceModel = PracticeModel.getOrCreatePracticeModel();
        practiceModel.addListener(this);

        this.lessonModel = LessonModel.getOrCreateLessonModel();
        lessonModel.addListener(this);

        this.practiceStrategy = AbstractPracticeStrategy.getOrCreateStrategyModel(practiceModel.getSelectedStrategy());
        practiceStrategy.addListener(this);

        this.vocabEntryModel = VocabEntryModel.getOrCreateVocabEntryModel();
        vocabEntryModel.addListener(this);

        EditorModel editorModel = EditorModel.getOrCreateEditorModel();
        editorModel.addListener(this);
    }

    public void modelCommandManagerCleared(ModelCommandManagerClearedEvent event) {
        onModelChange();
    }

    public void practiceChanged(ModelChangeEvent event) {
        onModelChange();
    }

    public void strategyChanged(ModelChangeEvent event) {
        onModelChange();
    }

    public void lessonChanged(ModelChangeEvent event, List<Lesson> affectedLessons) {
        try {
            deaf = true;
            vocabEntryModel.reSynchronize();
            practiceStrategy.reSynchronize();
        } finally {
            deaf = false;
        }
        onModelChange();
    }

    public void vocabEntryModelChanged(VocabEntryChangedEvent event) {
        // Performance: Leave strategy as is if the active set of vocab entries was not affected
        if (event.getChangeType() == VocabEntryChangedEvent.ChangeType.INACTIVE_SET) {
            practiceState.updateWindow(practiceModel, practiceWindow);
            return;
        }

        deaf = true;
        try {
            practiceStrategy.reSynchronize();
        } finally {
            deaf = false;
        }
        onModelChange();
    }

    @Override
    public void repetitionModeActivated(RepetitionMode repetitionMode) {
        if (repetitionMode == RepetitionMode.ON)
            practiceStrategy.reShuffle();
    }

    public void onWindowWasCreated() {
        if (lessonModel.countSelectedLessons() == 0) {
            if (lessonModel.countAvailableLessons() > 0)
                onOpenWindowType(WindowType.LESSONS_WINDOW);
            if (lessonModel.countSelectedLessons() == 0) {
                practiceState.updateWindow(practiceModel, practiceWindow);
                return;
            }
        }
        if (practiceState.isDisabled())
            practiceState.stateNext();
    }

    private void onModelChange() {
        if (deaf)
            return;

        int selectedLessons = lessonModel.countSelectedLessons();
        if (selectedLessons > 0 && practiceState.isDisabled())
            practiceState.stateAskNext();
        else if (selectedLessons == 0 && !practiceState.isDisabled())
            practiceState.stateDisable();

        practiceState.updateWindow(practiceModel, practiceWindow);
    }

    public void onNavigationForward() {
        practiceState.stateNext();
    }

    public void onNavigationBackward() {
        practiceState.stateAskNext();
        practiceStrategy.undo();
    }

    public void onNavigationReshuffle() {
        if (practiceStrategy.supportsGotoPositions()) {
            onNavigationGoto();
        } else {
            onNavigationReshuffleImpl();
        }
    }

    private void onNavigationReshuffleImpl() {
        practiceState.stateAskNext();
        practiceStrategy.reShuffle();
    }

    private void onNavigationGoto() {
        practiceWindow.askGotoPosition(practiceStrategy.getPracticeSize());
    }

    public void onNavigationReset() {
        practiceState.stateAskNext();
        practiceStrategy.reset();
    }

    @Override
    public NuiWindow onOpenWindowType(WindowType windowType) {
        // to prepare for FocusAware windows
        practiceState.fireFocusEvent(practiceWindow);

        return super.onOpenWindowType(windowType);
    }

    public PracticeState getPracticeState() {
        return practiceState;
    }

    public void setPracticeState(PracticeState practiceState) {
        this.practiceState = practiceState;
        practiceState.updateWindow(practiceModel, practiceWindow);
    }

    public void onPrepareAskNext() {
        practiceStrategy.executeCommand(new StrategyChange(practiceStrategy));
    }

    public void onLessonShowOptionChanged(boolean newValue) {
        practiceModel.setShowLessonFile(newValue);
    }

    public void onReverseOptionChanged(boolean reverse) {
        if (reverse)
            practiceModel.executeCommand(new PracticeModelChange(practiceModel, PracticeReverse.REVERSE));
        else
            practiceModel.executeCommand(new PracticeModelChange(practiceModel, PracticeReverse.NORMAL));
    }

    public void onImageOptionChanged(boolean image) {
        if (image)
            practiceModel.executeCommand(new PracticeModelChange(practiceModel, PracticeImage.IMAGE));
        else
            practiceModel.executeCommand(new PracticeModelChange(practiceModel, PracticeImage.TEXT));
    }

    public void onShowStatisticsOptionChanged(boolean showStatistics) {
        practiceModel.executeCommand(new PracticeModelChange(practiceModel, showStatistics));
    }

    public void onStrategyChanged(SelectedStrategy newStrategy) {
        if (practiceModel.getSelectedStrategy().equals(newStrategy))
            return;

        practiceStrategy.removeListener(this);
        practiceStrategy = AbstractPracticeStrategy.getOrCreateStrategyModel(newStrategy);
        practiceStrategy.reSynchronize();
        practiceStrategy.addListener(this);

        practiceState.stateAskNext();
        practiceModel.setSelectedStrategy(newStrategy);
    }

    public void onRepetitionActivated(boolean activated) {
        RepetitionMode newRepetitionMode = activated ? RepetitionMode.ON : RepetitionMode.OFF;
        RepetitionMode oldRepetitionMode = vocabEntryModel.getRepetitionMode();
        if (newRepetitionMode == oldRepetitionMode)
            return;

        practiceState.forceAskNext();
        vocabEntryModel.setRepetitionMode(newRepetitionMode);
    }

    public void onRepetitionAdd() {
        vocabEntryModel.addRepetition(practiceStrategy.currentRef());
    }

    public void onRepetitionRemove() {
        vocabEntryModel.removeRepetition(practiceStrategy.currentRef());

        if (practiceState.stateAskNextAvailable())
            practiceState.stateAskNext();
    }

    public void onRepetitionClear(boolean saveRepetitions) {
        if (saveRepetitions) {
            File file = Config.instance().autoSaveFile(
                    ConfigConstants.REPETITION_FILES_SUFFIX,
                    ConfigConstants.FILE_REF_EXTENSION
            );
            if (vocabEntryModel.saveRepetitions(file))
                practiceWindow.sendRepetitionSavedSuccess(file);
        }
        vocabEntryModel.clearRepetition();
    }

    public void editorChanged(ModelChangeEvent event) {
        vocabEntryModel.reSynchronize();
        practiceState.updateWindow(practiceModel, practiceWindow);
    }

    public void onEditEntry() {
        if (!practiceModel.hasCurrentRef())
            return;

        EditorController.editVocabEntry(practiceModel.getCurrentRef());
    }

    public void onAskGotoPositionResult(String input) {
        try {
            int pos = Integer.parseInt(input);
            if (pos >= 1 && pos <= practiceStrategy.getPracticeSize()) {
                practiceState.stateAskNext();
                practiceStrategy.gotoPosition(pos - 1);
                return;
            }
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.toString(), e);
        }
        onNavigationGoto();
    }

    public void onChangeRepetitionImpl(File repetitionFile) {
        try {
            VocabEntryModel.getOrCreateVocabEntryModel().setRepetitions(VocabIOHelper.fromRefFile(repetitionFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onRepetitionToggled() {
        boolean wasActivated = vocabEntryModel.getRepetitionMode() == RepetitionMode.ON;
        onRepetitionActivated(!wasActivated);
    }
}
