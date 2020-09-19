package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.model.commands.SimpleCommand;

public class StrategyChange implements SimpleCommand {

    private AbstractPracticeStrategy strategy;

    public StrategyChange(AbstractPracticeStrategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        strategy.gotoNextRef();
    }

    public void unExecute() {
        strategy.gotoPreviousRef();
    }
}
