package de.ebuchner.vocab.model.commands;

public interface SimpleCommand {
    void execute();

    void unExecute();
}