package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.managers.ServerEnviroment;
import utils.commands.Argument;
import utils.commands.Command;
import utils.commands.types.RequireAuthorization;
import utils.database.Vehicle;

/**
* Removes elements by id
*/
public class RemoveById extends Command implements RequireAuthorization {
    public RemoveById(String name, String description) {
        super(name, description);
        initArgs(new Argument<Long>("id", "Type id (integer): ", Vehicle.Caster.castToId));
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        var elementsBefore = ((ServerEnviroment) enviroment).getDatabaseManager().size();
        ((ServerEnviroment) enviroment).getDatabaseManager().removeElements(
            vehicle -> vehicle.getId() == (Long) args.get("id"), (String) args.get("username"), 1);
        var elementsAfter = ((ServerEnviroment) enviroment).getDatabaseManager().size();

        if (elementsBefore == elementsAfter) output.add("No items found");
        else output.add("Item was removed");
        return output;
    }
}
