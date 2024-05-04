package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Command;

/**
* Clears the collection
*/
public class Clear extends Command {
    public Clear(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute() {
        ArrayList<String> output = new ArrayList<String>();

        context.getEnviroment().getCollectionHandler().clear();
        output.add("Collection was cleared");

        return output;
    }
}
