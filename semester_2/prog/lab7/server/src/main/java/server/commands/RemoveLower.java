package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.managers.ServerEnviroment;
import utils.commands.Argument;
import utils.commands.Command;
import utils.commands.types.RequireAuthorization;
import utils.database.Vehicle;

/**
* Removes elements that less than inputed object from the collection
*/
public class RemoveLower extends Command implements RequireAuthorization {
    public RemoveLower(String name, String description) {
        super(name, description);
        initArgs(new Argument<Long>("id", "Input vehicle id (integer): ", Vehicle.Caster.castToId));
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        var output = new ArrayList<String>();

        var elementsBefore = ((ServerEnviroment) enviroment).getDatabaseManager().size();
        ((ServerEnviroment) enviroment).getDatabaseManager().removeElements(
            vehicle -> vehicle.getId() < (Long) args.get("id"), (String) args.get("username"));
        var elementsAfter = ((ServerEnviroment) enviroment).getDatabaseManager().size();

        if (elementsBefore == elementsAfter) output.add("No items found");
        else output.add(elementsBefore - elementsAfter + " item(s) was removed");
        return output;
    }
}
