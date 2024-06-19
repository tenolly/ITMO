package server.commands;

import server.managers.ServerEnviroment;
import shared.commands.Command;
import shared.commands.types.RequireAuthorization;
import shared.request.CommandRequest;

/**
* Clears the collection
*/
public class Clear extends Command implements RequireAuthorization {
    public Clear(String name, String description) {
        super(name, description);
    }

    @Override
    public CommandRequest execute(CommandRequest request) {
        ((ServerEnviroment) enviroment).getDatabaseManager().clear(request.getUser().getUsername());
        request.setResponse("Collection was cleared");

        return request;
    }
}
