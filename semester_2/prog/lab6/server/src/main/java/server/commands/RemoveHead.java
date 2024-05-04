package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.handlers.ServerEnviroment;
import utils.commands.Command;

/**
* Returns and removes first element from the collection
*/
public class RemoveHead extends Command {
    public RemoveHead(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        var collectionHandler = ((ServerEnviroment) enviroment).getCollectionHandler();

        if (collectionHandler.size() == 0) {
            output.add("Collection is empty");
        } else {
            output.add(collectionHandler.pop().toString());
            output.add("First element was removed");
        }

        return output;
    }
}
