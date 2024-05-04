package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Argument;
import lab5.commands.core.Command;
import lab5.vehicle.Vehicle;

/**
* Counts objects that less than inputed by engine power value
*/
public class CountLessThanEnginePower extends Command {
    public CountLessThanEnginePower(String name, String description) {
        super(name, description);
        arguments.add(new Argument<Float>("enginePower", 
            "Input engine power " + Vehicle.Validator.enginePowerHumanIntelligibleConditions + ": ", Vehicle.Caster.castToEnginePower));
    }

    @Override
    public ArrayList<String> execute() {
        var output = new ArrayList<String>();

        var cache = context.readArguments();
        int count = context.getEnviroment().getCollectionHandler().countElements(
            vehicle -> vehicle.getEnginePower() < (Float) cache.get("enginePower"));

        output.add("Found " + count + " items");
        return output;
    }
}
