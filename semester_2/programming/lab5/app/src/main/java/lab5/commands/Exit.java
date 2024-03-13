package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Command;

/**
* Terminates the program
*/
public class Exit extends Command {
    public Exit(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute() {
        System.exit(0);
        return null;
    }
}
