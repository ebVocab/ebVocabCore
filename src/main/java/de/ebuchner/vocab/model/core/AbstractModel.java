package de.ebuchner.vocab.model.core;

import de.ebuchner.vocab.config.preferences.PreferencesSupport;
import de.ebuchner.vocab.model.commands.CommandManager;
import de.ebuchner.vocab.model.commands.SimpleCommand;
import de.ebuchner.vocab.model.commands.UndoRedoSupport;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModel<C extends SimpleCommand, L extends ModelListener>
        implements PreferencesSupport, UndoRedoSupport<C> {

    private CommandManager<C> commandManager = new CommandManager<C>();
    protected List<L> listeners = new ArrayList<L>();

    public final void executeCommands(List<C> commands) {
        for (C command : commands) {
            commandManager.addCommand(command);
            commandManager.execute();
        }
        fireModelChanged();
    }

    public final void executeCommand(C command) {
        commandManager.addCommand(command);
        commandManager.execute();
        fireModelChanged();
    }

    public boolean canUndo() {
        return commandManager.canUndo();
    }

    public boolean canRedo() {
        return commandManager.canRedo();
    }

    public C undo() {
        C command = commandManager.undo();
        fireModelChanged();
        return command;
    }

    public C redo() {
        C command = commandManager.redo();
        fireModelChanged();
        return command;
    }

    public boolean isDirty() {
        return commandManager.isDirty();
    }

    public void addListener(L listener) {
        listeners.add(listener);
    }

    public void removeListener(L listener) {
        listeners.remove(listener);
    }

    protected abstract void fireModelChanged();

    protected final void clearCommands() {
        commandManager.clear();
        fireModelCommandManagerCleared();
    }

    private void fireModelCommandManagerCleared() {
        ModelCommandManagerClearedEvent clearedEvent = new ModelCommandManagerClearedEvent(this);
        for (L listener : listeners) {
            listener.modelCommandManagerCleared(clearedEvent);
        }
    }

    protected List<C> undoableCommands() {
        return commandManager.undoableCommands();
    }

    public abstract void reSynchronize();
}
