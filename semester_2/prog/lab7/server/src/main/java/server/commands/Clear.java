package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.managers.ServerEnviroment;
import utils.commands.Command;
import utils.commands.types.RequireAuthorization;

/**
* Clears the collection
*/
public class Clear extends Command implements RequireAuthorization {
    public Clear(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        System.out.println(args.values());

        ((ServerEnviroment) enviroment).getDatabaseManager().clear((String) args.get("username"));
        output.add("Collection was cleared");

        return output;
    }
}
