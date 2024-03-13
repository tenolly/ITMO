package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Command;

/**
* Removes first element from the collection
*/
public class RemoveFirst extends Command {
    public RemoveFirst(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute() {
        ArrayList<String> output = new ArrayList<String>();

        var collectionHandler = context.getEnviroment().getCollectionHandler();

        if (collectionHandler.size() == 0) {
            output.add("Collection is empty");
        } else {
            collectionHandler.pop();
            output.add("First element was removed");
        }

        return output;
    }
}
