package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.handlers.ServerEnviroment;
import utils.commands.Command;

/**
* Returns information about the collection
*/
public class Info extends Command {
    public Info(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        var collectionHandler = ((ServerEnviroment) enviroment).getCollectionHandler();
        output.add("  Type of the collection: " + collectionHandler.type().toString());
        output.add("  The collection created at " + collectionHandler.createdAt().toString());
        output.add("  Number of items in the collection: " + collectionHandler.size());

        return output;
    }
}
