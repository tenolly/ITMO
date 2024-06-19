package server.commands;

import java.util.ArrayList;

import server.managers.ServerEnviroment;
import shared.commands.Command;
import shared.commands.types.RequireAuthorization;
import shared.request.CommandRequest;

/**
* Returns information about the collection
*/
public class Info extends Command implements RequireAuthorization {
    public Info(String name, String description) {
        super(name, description);
    }

    @Override
    public CommandRequest execute(CommandRequest request) {
        ArrayList<String> output = new ArrayList<String>();

        var databaseManager = ((ServerEnviroment) enviroment).getDatabaseManager();
        output.add(databaseManager.type().toString());  // Type of the collection
        output.add(databaseManager.createdAt().toString()); // The collection created at
        output.add(Integer.toString(databaseManager.size()));  // Number of items in the collection

        request.setResponse(String.join("|", output));
        
        return request;
    }
}
