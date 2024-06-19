package server.commands;

import java.util.HashMap;

import server.managers.ServerEnviroment;
import shared.caster.CasterUtil;
import shared.commands.Argument;
import shared.commands.Command;
import shared.commands.types.RequireAuthorization;
import shared.database.Vehicle;
import shared.request.CommandRequest;

/**
* Removes elements that less than inputed object from the collection
*/
public class RemoveLower extends Command implements RequireAuthorization {
    public RemoveLower(String name, String description) {
        super(name, description);
        initArgs(new Argument<Long>("id", "Input vehicle id (integer): ", Vehicle.Caster.castToId));
    }

    @Override
    public CommandRequest execute(CommandRequest request) {
        HashMap<String, ?> args = request.getArgs();

        var elementsBefore = ((ServerEnviroment) enviroment).getDatabaseManager().size();
        ((ServerEnviroment) enviroment).getDatabaseManager().removeElements(
            vehicle -> vehicle.getId() < CasterUtil.castToLong.apply((String) args.get("id")), 
            request.getUser().getUsername());
        var elementsAfter = ((ServerEnviroment) enviroment).getDatabaseManager().size();

        if (elementsBefore == elementsAfter) request.setResponse("No items found");
        else request.setResponse(Integer.toString(elementsBefore - elementsAfter));

        return request;
    }
}
