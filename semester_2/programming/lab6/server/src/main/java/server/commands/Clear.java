package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.handlers.ServerEnviroment;
import utils.commands.Command;

/**
* Clears the collection
*/
public class Clear extends Command {
    public Clear(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        ((ServerEnviroment) enviroment).getCollectionHandler().clear();
        output.add("Collection was cleared");

        return output;
    }
}
