package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Argument;
import lab5.commands.core.Command;
import lab5.vehicle.Vehicle;
import lab5.vehicle.components.Coordinates;
import lab5.vehicle.components.FuelType;

/**
* Adds new object to the collection
*/
public class Add extends Command {
    public Add(String name, String description) {
        super(name, description);
        arguments.add(new Argument<String>("name",
            "Input name " + Vehicle.Validator.nameHumanIntelligibleConditions + ": ", Vehicle.Caster.castToName));
        arguments.add(new Argument<Double>("x",
            "Input x " + Coordinates.Validator.xHumanIntelligibleConditions + ": ", Coordinates.Caster.castToX));
        arguments.add(new Argument<Float>("y", 
            "Input y " + Coordinates.Validator.yHumanIntelligibleConditions + ": ", Coordinates.Caster.castToY));
        arguments.add(new Argument<Float>("enginePower", 
            "Input engine power " + Vehicle.Validator.enginePowerHumanIntelligibleConditions + ": ", Vehicle.Caster.castToEnginePower));
        arguments.add(new Argument<Integer>("numberOfWheels", 
            "Input number of wheels " + Vehicle.Validator.numberOfWheelsHumanIntelligibleConditions + ": ", Vehicle.Caster.castToNumberOfWheels));
        arguments.add(new Argument<Float>("fuelConsumption", 
            "Input fuel consumption " + Vehicle.Validator.fuelConsumptionHumanIntelligibleConditions + ": ", Vehicle.Caster.castToFuelConsumption));
        arguments.add(new Argument<FuelType>("fuelType", 
            "Input fuel type " + Vehicle.Validator.fuelTypeHumanIntelligibleConditions + ": ", Vehicle.Caster.castToFuelType));
    }

    @Override
    public ArrayList<String> execute() {
        ArrayList<String> output = new ArrayList<String>();
        
        var cache = context.readArguments();

        context.getEnviroment().getCollectionHandler().add(new Vehicle(
            (String) cache.get("name"), 
            new Coordinates((Double) cache.get("x"), (Float) cache.get("y")),
            (Float) cache.get("enginePower"),
            (Integer) cache.get("numberOfWheels"),
            (Float) cache.get("fuelConsumption"),
            (FuelType) cache.get("fuelType")
        ));

        output.add("New Vehicle was created");
        return output;
    }
}
