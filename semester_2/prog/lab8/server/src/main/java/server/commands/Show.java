package server.commands;

import java.util.ArrayList;

import server.managers.ServerEnviroment;
import shared.commands.Command;
import shared.commands.types.RequireAuthorization;
import shared.request.CommandRequest;

/**
* Outputs the collection
*/
public class Show extends Command implements RequireAuthorization {
    public Show(String name, String description) {
        super(name, description);
    }

    @Override
    public CommandRequest execute(CommandRequest request) {
        ArrayList<String> output = new ArrayList<String>();

        int counter = 1;
        for(Object obj : ((ServerEnviroment) enviroment).getDatabaseManager().getCollection()) {
            output.add("" + counter++ + ". " + obj.toString());
        };

        if (counter == 1) {
            output.add("Collection is empty");
        }

        request.setSharedData(((ServerEnviroment) enviroment).getDatabaseManager().getCollection());
        request.setResponse(String.join("\n", output));

        return request;
    }
}
