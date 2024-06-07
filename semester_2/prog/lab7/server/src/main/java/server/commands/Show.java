package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.managers.ServerEnviroment;
import utils.commands.Command;
import utils.commands.types.RequireAuthorization;

/**
* Outputs the collection
*/
public class Show extends Command implements RequireAuthorization {
    public Show(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        int counter = 1;
        for(Object obj : ((ServerEnviroment) enviroment).getDatabaseManager().getCollection()) {
            output.add("  " + counter++ + ". " + obj.toString());
        };

        if (counter == 1) {
            output.add("Collection is empty");
        }

        return output;
    }
}
