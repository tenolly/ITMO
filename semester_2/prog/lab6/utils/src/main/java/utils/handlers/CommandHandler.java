package utils.handlers;

import java.util.Collection;
import java.util.HashMap;

import utils.commands.Command;

/**
* Stores and provides interaction with the commands
*/
public class CommandHandler {
    private HashMap<String, Command> commands = new HashMap<String, Command>();

    public CommandHandler add(Command command) throws IllegalArgumentException {
        if (commands.keySet().contains(command.getName())) {
            throw new IllegalArgumentException("alias " + command.getName() + " is already exists");
        }

        commands.put(command.getName().intern(), command);
        return this;
    }

    public Command getCommand(String name) {
        return commands.get(name.intern());
    }

    public Collection<Command> getAllCommands() {
        return commands.values();
    }
}
