package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Command;

/**
* Outputs the collection
*/
public class Show extends Command {
    public Show(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute() {
        ArrayList<String> output = new ArrayList<String>();

        int counter = 1;
        for(Object obj : context.getEnviroment().getCollectionHandler().getCollection()) {
            output.add("  " + counter++ + ". " + obj.toString());
        };

        if (counter == 1) {
            output.add("Collection is empty");
        }

        return output;
    }
}
