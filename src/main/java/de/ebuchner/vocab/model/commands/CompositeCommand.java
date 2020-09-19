package de.ebuchner.vocab.model.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeCommand implements SimpleCommand {
    private List<SimpleCommand> commands = new ArrayList<SimpleCommand>();

    public void execute() {
        for (SimpleCommand command : commands) {
            command.execute();
        }
    }

    public void unExecute() {
        List<SimpleCommand> reverseList = new ArrayList<SimpleCommand>();
        reverseList.addAll(commands);
        Collections.reverse(reverseList);

        for (SimpleCommand command : reverseList) {
            command.unExecute();
        }
    }

    public List<SimpleCommand> getCommands() {
        return commands;
    }
}
