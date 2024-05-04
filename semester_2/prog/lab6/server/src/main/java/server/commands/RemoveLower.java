package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.handlers.ServerEnviroment;
import utils.commands.Argument;
import utils.commands.Command;
import utils.vehicle.Vehicle;

/**
* Removes elements that less than inputed object from the collection
*/
public class RemoveLower extends Command  {
    public RemoveLower(String name, String description) {
        super(name, description);
        initArgs(new Argument<Long>("id", "Input vehicle id (integer): ", Vehicle.Caster.castToId));
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        var output = new ArrayList<String>();

        var elementsBefore = ((ServerEnviroment) enviroment).getCollectionHandler().size();
        ((ServerEnviroment) enviroment).getCollectionHandler().removeElements(
            vehicle -> vehicle.getId() < (Long) args.get("id"));
        var elementsAfter = ((ServerEnviroment) enviroment).getCollectionHandler().size();

        if (elementsBefore == elementsAfter) output.add("No items found");
        else output.add(elementsBefore - elementsAfter + " item(s) was removed");
        return output;
    }
}
