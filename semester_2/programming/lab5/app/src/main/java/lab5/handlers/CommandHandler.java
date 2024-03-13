package lab5.handlers;

import java.util.Collection;
import java.util.HashMap;

import lab5.commands.core.Command;

/**
* Stores and provides interaction with the commands
*/
public class CommandHandler {
    private HashMap<String, Command> commands = new HashMap<String, Command>();

    public CommandHandler add(Command command) throws IllegalArgumentException {
        if (commands.keySet().contains(command.getName())) {
            throw new IllegalArgumentException("alias " + command.getName() + " is already exists");
        }

        commands.put(command.getName(), command);
        return this;
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public Collection<Command> getAllCommands() {
        return commands.values();
    }
}
