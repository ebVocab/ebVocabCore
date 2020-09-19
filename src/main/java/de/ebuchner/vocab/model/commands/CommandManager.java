package de.ebuchner.vocab.model.commands;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager<C extends SimpleCommand> implements UndoRedoSupport {

    private List<C> commands = new ArrayList<C>();
    private List<C> unDoable = new ArrayList<C>();
    private List<C> reDoable = new ArrayList<C>();

    public void addCommand(C command) {
        commands.add(command);
        reDoable.clear();
    }

    public boolean canUndo() {
        return !unDoable.isEmpty();
    }

    public boolean canRedo() {
        return !reDoable.isEmpty();
    }

    public C redo() {
        if (!canRedo())
            throw new IllegalStateException();
        C command = reDoable.remove(0);
        command.execute();
        unDoable.add(command);
        return command;
    }

    public C undo() {
        if (!canUndo())
            throw new IllegalStateException();
        commands.clear(); // or better add to redo?
        C command = unDoable.remove(unDoable.size() - 1);
        command.unExecute();
        reDoable.add(0, command);
        return command;
    }

    public boolean canExecute() {
        return !commands.isEmpty();
    }

    public void execute() {
        if (!canExecute())
            throw new IllegalStateException("cannot execute");
        C command = commands.remove(0);
        command.execute();
        unDoable.add(command);
    }

    public boolean isDirty() {
        return canUndo();
    }

    public void clear() {
        commands.clear();
        unDoable.clear();
        reDoable.clear();
    }

    public List<C> undoableCommands() {
        return Collections.unmodifiableList(unDoable);
    }
}
