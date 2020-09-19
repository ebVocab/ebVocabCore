package de.ebuchner.vocab.nui;

import de.ebuchner.toolbox.i18n.I18NContext;
import de.ebuchner.vocab.config.Config;
import de.ebuchner.vocab.config.fields.Field;
import de.ebuchner.vocab.config.fields.FieldFactory;
import de.ebuchner.vocab.model.VocabModel;
import de.ebuchner.vocab.model.lessons.LessonModel;
import de.ebuchner.vocab.model.lessons.entry.VocabEntry;
import de.ebuchner.vocab.model.nui.NuiWindow;
import de.ebuchner.vocab.model.nui.NuiWindowParameter;
import de.ebuchner.vocab.model.nui.WindowType;
import de.ebuchner.vocab.model.nui.focus.FocusAware;
import de.ebuchner.vocab.model.nui.focus.NuiTextFieldWithFocus;
import de.ebuchner.vocab.model.project.ProjectConfiguration;
import de.ebuchner.vocab.model.project.ProjectModel;
import de.ebuchner.vocab.nui.common.I18NLocator;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

public abstract class NuiDirector {

    private I18NContext i18n = I18NLocator.locate();

    public NuiWindow showWindow(WindowType windowType) {
        return showWindow(windowType, NuiWindowParameter.EMPTY);
    }

    public NuiWindow showWindow(WindowType windowType, NuiWindowParameter windowParameter) {
        if (windowType == null)
            throw new IllegalArgumentException("Window type must not be null");

        NuiWindow nuiWindow = createWindow(windowType, windowParameter);
        if (nuiWindow == null)
            return null;
        nuiWindow.nuiWindowShow(windowParameter);

        if (nuiWindow instanceof FocusAware) {
            fireFocusEventToSingleWindow((FocusAware) nuiWindow);
        }

        return nuiWindow;
    }

    protected void fireFocusEventToSingleWindow(FocusAware nuiWindow) {
        NuiTextFieldWithFocus textFieldWithFocus = getLastTextFieldWithFocus();
        if (textFieldWithFocus != null)
            nuiWindow.onFocusChangedTo(textFieldWithFocus);
    }

    protected abstract NuiWindow createWindow(WindowType windowType, NuiWindowParameter windowParameter);

    public NuiTextFieldWithFocus getLastTextFieldWithFocus() {
        return null;
    }

    public void sendWindow(NuiWindowParameter windowParameter) {

    }

    public String uiDescriptionWithOptHint(Field field) {
        String description = uiDescription(field);
        if (field.optional())
            description = String.format("%s %s", description, i18n.getString("nui.editor.optional"));

        return description;
    }

    public String uiDescription(Field field) {
        if (field.name().equals(FieldFactory.FOREIGN)) {
            if (Config.projectInitialized())
                return new Locale(Config.instance().getLocale()).getDisplayLanguage();
        }

        if (field.name().equals(FieldFactory.USER)) {
            Locale userLocale = i18n.getLocale();
            return userLocale.getDisplayLanguage(userLocale);
        }

        String resKey = String.format("nui.vocab.field.%s.description", field.name());
        try {
            return i18n.getString(resKey);
        } catch (MissingResourceException mre) {
            System.out.println("Missing field description " + resKey);
        }
        return field.name();
    }

    public void startUp() {
        ProjectModel projectModel = ProjectModel.getInstance();
        File projectDirectory = projectModel.getProjectDirectory();
        if (projectDirectory == null) {
            showWindow(WindowType.PROJECT_WINDOW);

            projectDirectory = projectModel.getProjectDirectory();
            if (projectDirectory == null) {
                System.out.println("Application initialisation canceled. Shutting down...");
                return;
            }
        }

        ProjectConfiguration.startupWithProjectDirectory(projectDirectory);
        startUpSystemDependent();
        startDefaultWindow();
    }

    private void startDefaultWindow() {
        if (LessonModel.getOrCreateLessonModel().countAvailableLessons() > 0)
            showWindow(WindowType.PRACTICE_WINDOW);
        else
            showWindow(WindowType.EDITOR_WINDOW);
    }

    protected void startUpSystemDependent() {

    }

    protected void shutDownImpl(boolean doExit) {
        System.out.println("VocabApplication shutting down...");
        shutDownSystemDependant();
        VocabModel.getInstance().shutDown();
        if (Config.projectInitialized())
            Config.instance().dispose();

        if (doExit)
            exitSystemDependant();
    }

    protected void shutDownSystemDependant() {

    }

    protected abstract void exitSystemDependant();

    public boolean shutDown() {
        if (!closeAll())
            return false;
        shutDownImpl(false);
        return true;
    }

    public final void reboot() {
        if (!closeAll())
            return;
        shutDownImpl(false);
        startUp();
    }

    public abstract boolean closeAll();

    public abstract boolean entriesToClipboard(List<VocabEntry> entriesToCopy);

    public abstract List<VocabEntry> entriesFromClipboard();
}
