package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.managers.ServerEnviroment;
import utils.commands.Argument;
import utils.commands.Command;
import utils.commands.types.RequireAuthorization;
import utils.database.Vehicle;
import utils.database.components.vehicle.Coordinates;
import utils.database.components.vehicle.FuelType;

/**
* Adds new object to the collection
*/
public class Add extends Command implements RequireAuthorization {
    public Add(String name, String description) {
        super(name, description);
        this.initArgs(
            new Argument<String>(
                "name", 
                "Input name " + Vehicle.Validator.nameHumanIntelligibleConditions + ": ", 
                Vehicle.Caster.castToName
            ),
            new Argument<Double>(
                "x",
                "Input x " + Coordinates.Validator.xHumanIntelligibleConditions + ": ", 
                Coordinates.Caster.castToX
            ),
            new Argument<Float>(
                "y", 
                "Input y " + Coordinates.Validator.yHumanIntelligibleConditions + ": ", 
                Coordinates.Caster.castToY
            ),
            new Argument<Float>(
                "enginePower", 
                "Input engine power " + Vehicle.Validator.enginePowerHumanIntelligibleConditions + ": ", 
                Vehicle.Caster.castToEnginePower
            ),
            new Argument<Integer>(
                "numberOfWheels", 
                "Input number of wheels " + Vehicle.Validator.numberOfWheelsHumanIntelligibleConditions + ": ", 
                Vehicle.Caster.castToNumberOfWheels
            ),
            new Argument<Float>(
                "fuelConsumption", 
                "Input fuel consumption " + Vehicle.Validator.fuelConsumptionHumanIntelligibleConditions + ": ", 
                Vehicle.Caster.castToFuelConsumption
            ),
            new Argument<FuelType>(
                "fuelType", 
                "Input fuel type " + Vehicle.Validator.fuelTypeHumanIntelligibleConditions + ": ", 
                Vehicle.Caster.castToFuelType
            )
        );
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        ((ServerEnviroment) enviroment).getDatabaseManager().add(new Vehicle(
            (String) args.get("name"), 
            new Coordinates((Double) args.get("x"), (Float) args.get("y")),
            (Float) args.get("enginePower"),
            (Integer) args.get("numberOfWheels"),
            (Float) args.get("fuelConsumption"),
            (FuelType) args.get("fuelType"),
            (String) args.get("username")
        ));

        output.add("New Vehicle was created");
        return output;
    }
}
