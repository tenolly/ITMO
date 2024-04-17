package client.commands;

import java.util.ArrayList;
import java.util.HashMap;

import client.managers.ClientEnviroment;
import utils.commands.Command;

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

        ((ClientEnviroment) enviroment).getCommandHandler().getAllCommands().forEach((cmd) -> {
            output.add(String.format("  %s - %s", cmd.getName(), cmd.getDescription()));
        });

        output.sort(String::compareTo);

        return output;
    }
}
