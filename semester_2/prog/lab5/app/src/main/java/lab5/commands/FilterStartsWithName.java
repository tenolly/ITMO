package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Argument;
import lab5.commands.core.Command;
import lab5.vehicle.Vehicle;

/**
* Returns objects that starts with inputed name
*/
public class FilterStartsWithName extends Command {
    public FilterStartsWithName(String name, String description) {
        super(name, description);
        arguments.add(new Argument<String>("name",
            "Input name " + Vehicle.Validator.nameHumanIntelligibleConditions + ": ", Vehicle.Caster.castToName));
    }

    @Override
    public ArrayList<String> execute() {
        var output = new ArrayList<String>();

        var cache = context.readArguments();
        var elements = context.getEnviroment().getCollectionHandler().getElements(
            vehicle -> vehicle.getName().startsWith((String) cache.get("name")));

        if (elements.size() != 0) {
            elements.forEach(element -> output.add(element.toString()));
        } else {
            output.add("No items found");
        }
        
        return output;
    }
}
