package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Argument;
import lab5.commands.core.Command;
import lab5.vehicle.Vehicle;

/**
* Returns objects that greater than inputed by fuel consumption value
*/
public class FilterGreaterThanFuelConsumption extends Command {
    public FilterGreaterThanFuelConsumption(String name, String description) {
        super(name, description);
        arguments.add(new Argument<Float>("fuelConsumption", 
            "Input fuel consumption " + Vehicle.Validator.fuelConsumptionHumanIntelligibleConditions + ": ", Vehicle.Caster.castToFuelConsumption));
    }

    @Override
    public ArrayList<String> execute() {
        var output = new ArrayList<String>();

        var cache = context.readArguments();
        var elements = context.getEnviroment().getCollectionHandler().getElements(
            vehicle -> vehicle.getFuelConsumption() < (Float) cache.get("fuelConsumption"));

        if (elements.size() != 0) {
            elements.forEach(element -> output.add(element.toString()));
        } else {
            output.add("No items found");
        }
        
        return output;
    }
}
