package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.handlers.ServerEnviroment;
import utils.commands.Argument;
import utils.commands.Command;
import utils.vehicle.Vehicle;

/**
* Returns objects that greater than inputed by fuel consumption value
*/
public class FilterGreaterThanFuelConsumption extends Command {
    public FilterGreaterThanFuelConsumption(String name, String description) {
        super(name, description);
        initArgs(
            new Argument<Float>(
                "fuelConsumption", 
                "Input fuel consumption " + Vehicle.Validator.fuelConsumptionHumanIntelligibleConditions + ": ", Vehicle.Caster.castToFuelConsumption
            )
        );
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        var output = new ArrayList<String>();

        var elements = ((ServerEnviroment) enviroment).getCollectionHandler().getElements(
            vehicle -> vehicle.getFuelConsumption() < (Float) args.get("fuelConsumption"));

        if (elements.size() != 0) {
            elements.forEach(element -> output.add(element.toString()));
        } else {
            output.add("No items found");
        }
        
        return output;
    }
}
