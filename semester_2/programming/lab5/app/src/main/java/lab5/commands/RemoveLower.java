package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Argument;
import lab5.commands.core.Command;
import lab5.vehicle.Vehicle;

/**
* Removes elements that less than inputed object from the collection
*/
public class RemoveLower extends Command{
    public RemoveLower(String name, String description) {
        super(name, description);
        arguments.add(new Argument<Long>("id", "Input vehicle id (integer): ", Vehicle.Caster.castToId));
    }

    @Override
    public ArrayList<String> execute() {
        var output = new ArrayList<String>();

        var cache = context.readArguments();

        var elementsBefore = context.getEnviroment().getCollectionHandler().size();
        context.getEnviroment().getCollectionHandler().removeElements(
            vehicle -> vehicle.getId() < (Long) cache.get("id"));
        var elementsAfter = context.getEnviroment().getCollectionHandler().size();

        if (elementsBefore == elementsAfter) output.add("No items found");
        else output.add(elementsBefore - elementsAfter + " item(s) was removed");
        return output;
    }
}
