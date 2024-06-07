package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.managers.ServerEnviroment;
import utils.commands.Command;
import utils.commands.types.RequireAuthorization;

/**
* Returns and removes first element from the collection
*/
public class RemoveHead extends Command implements RequireAuthorization {
    public RemoveHead(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        var databaseManager = ((ServerEnviroment) enviroment).getDatabaseManager();

        if (databaseManager.size() == 0) {
            output.add("Collection is empty");
        } else {
            output.add(databaseManager.pop((String) args.get("username")).toString());
            output.add("First element was removed");
        }

        return output;
    }
}
