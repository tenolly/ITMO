package client.commands;

import java.util.ArrayList;
import java.util.HashMap;

import utils.commands.Command;

/**
* Terminates the program
*/
public class Exit extends Command {
    public Exit(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        System.exit(0);
        return null;
    }
}
