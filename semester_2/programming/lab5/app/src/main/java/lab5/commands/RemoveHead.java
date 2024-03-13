package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Command;

/**
* Returns and removes first element from the collection
*/
public class RemoveHead extends Command {
    public RemoveHead(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute() {
        ArrayList<String> output = new ArrayList<String>();

        var collectionHandler = context.getEnviroment().getCollectionHandler();

        if (collectionHandler.size() == 0) {
            output.add("Collection is empty");
        } else {
            output.add(collectionHandler.pop().toString());
            output.add("First element was removed");
        }

        return output;
    }
}
