package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.managers.ServerEnviroment;
import utils.commands.Command;
import utils.commands.types.RequireAuthorization;

/**
* Returns information about the collection
*/
public class Info extends Command implements RequireAuthorization {
    public Info(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        var databaseManager = ((ServerEnviroment) enviroment).getDatabaseManager();
        output.add("  Type of the collection: " + databaseManager.type().toString());
        output.add("  The collection created at " + databaseManager.createdAt().toString());
        output.add("  Number of items in the collection: " + databaseManager.size());

        return output;
    }
}
