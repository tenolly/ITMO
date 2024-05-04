package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Argument;
import lab5.commands.core.Command;
import lab5.vehicle.Vehicle;

/**
* Removes elements by id
*/
public class RemoveById extends Command {
    public RemoveById(String name, String description) {
        super(name, description);
        arguments.add(new Argument<Long>("id", "Type id (integer): ", Vehicle.Caster.castToId));
    }

    @Override
    public ArrayList<String> execute() {
        ArrayList<String> output = new ArrayList<String>();

        var cache = context.readArguments();

        var elementsBefore = context.getEnviroment().getCollectionHandler().size();
        context.getEnviroment().getCollectionHandler().removeElements(
            vehicle -> vehicle.getId() == (Long) cache.get("id"), 1);
        var elementsAfter = context.getEnviroment().getCollectionHandler().size();

        if (elementsBefore == elementsAfter) output.add("No items found");
        else output.add("Item was removed");
        return output;
    }
}
