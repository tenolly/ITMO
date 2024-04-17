package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.handlers.ServerEnviroment;
import utils.commands.Argument;
import utils.commands.Command;
import utils.vehicle.Vehicle;

/**
* Returns objects that starts with inputed name
*/
public class FilterStartsWithName extends Command {
    public FilterStartsWithName(String name, String description) {
        super(name, description);
        initArgs(
            new Argument<String>(
                "name",
                "Input name " + Vehicle.Validator.nameHumanIntelligibleConditions + ": ", Vehicle.Caster.castToName
            )
        );
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        var output = new ArrayList<String>();

        var elements = ((ServerEnviroment) enviroment).getCollectionHandler().getElements(
            vehicle -> vehicle.getName().startsWith((String) args.get("name")));

        if (elements.size() != 0) {
            elements.forEach(element -> output.add(element.toString()));
        } else {
            output.add("No items found");
        }
        
        return output;
    }
}
