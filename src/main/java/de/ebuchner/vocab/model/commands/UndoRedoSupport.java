package de.ebuchner.vocab.model.commands;

public interface UndoRedoSupport<C extends SimpleCommand> {

    boolean canUndo();

    C undo();

    boolean canRedo();

    C redo();

    boolean isDirty();
}
