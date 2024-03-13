package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Command;

/**
* Returns help information about commands
*/
public class Help extends Command {
    public Help(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute() {
        var output = new ArrayList<String>();

        context.getEnviroment().getCommandHandler().getAllCommands().forEach((cmd) -> {
            output.add(String.format("  %s - %s", cmd.getName(), cmd.getDescription()));
        });

        output.sort(String::compareTo);

        return output;
    }
}
