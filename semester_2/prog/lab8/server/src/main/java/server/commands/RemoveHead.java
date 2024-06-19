package server.commands;

import server.managers.ServerEnviroment;
import shared.commands.Command;
import shared.commands.types.RequireAuthorization;
import shared.request.CommandRequest;

/**
* Returns and removes first element from the collection
*/
public class RemoveHead extends Command implements RequireAuthorization {
    public RemoveHead(String name, String description) {
        super(name, description);
    }

    @Override
    public CommandRequest execute(CommandRequest request) {
        var databaseManager = ((ServerEnviroment) enviroment).getDatabaseManager();

        if (databaseManager.size() == 0) {
            request.setResponse("Collection is empty");
        } else {
            var res = databaseManager.pop(request.getUser().getUsername());
            if (res == null) request.setResponse("No items found");
            else request.setResponse(res.toString());
        }

        return request;
    }
}
