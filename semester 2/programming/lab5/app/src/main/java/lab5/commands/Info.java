package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Command;

/**
* Returns information about the collection
*/
public class Info extends Command {
    public Info(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute() {
        ArrayList<String> output = new ArrayList<String>();

        var collectionHandler = context.getEnviroment().getCollectionHandler();
        output.add("  Type of the collection: " + collectionHandler.type().toString());
        output.add("  The collection created at " + collectionHandler.createdAt().toString());
        output.add("  Number of items in the collection: " + collectionHandler.size());

        return output;
    }
}
