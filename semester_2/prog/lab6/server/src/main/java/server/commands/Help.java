package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.handlers.ServerEnviroment;
import utils.commands.Command;
import utils.commands.types.OnlyForServer;

/**
* Returns help information about commands
*/
public class Help extends Command {
    public Help(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        var output = new ArrayList<String>();

        ((ServerEnviroment) enviroment).getCommandHandler().getAllCommands().forEach((cmd) -> {
            if (!(cmd instanceof OnlyForServer)) output.add(String.format("  %s - %s", cmd.getName(), cmd.getDescription()));
        });

        output.sort(String::compareTo);

        return output;
    }
}
