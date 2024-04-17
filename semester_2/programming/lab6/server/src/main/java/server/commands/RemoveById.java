package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.handlers.ServerEnviroment;
import utils.commands.Argument;
import utils.commands.Command;
import utils.vehicle.Vehicle;

/**
* Removes elements by id
*/
public class RemoveById extends Command {
    public RemoveById(String name, String description) {
        super(name, description);
        initArgs(new Argument<Long>("id", "Type id (integer): ", Vehicle.Caster.castToId));
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        var elementsBefore = ((ServerEnviroment) enviroment).getCollectionHandler().size();
        ((ServerEnviroment) enviroment).getCollectionHandler().removeElements(
            vehicle -> vehicle.getId() == (Long) args.get("id"), 1);
        var elementsAfter = ((ServerEnviroment) enviroment).getCollectionHandler().size();

        if (elementsBefore == elementsAfter) output.add("No items found");
        else output.add("Item was removed");
        return output;
    }
}
