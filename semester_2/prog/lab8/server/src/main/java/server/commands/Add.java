package server.commands;

import java.util.HashMap;

import server.managers.ServerEnviroment;
import shared.commands.Argument;
import shared.commands.Command;
import shared.commands.types.RequireAuthorization;
import shared.database.Vehicle;
import shared.database.components.vehicle.Coordinates;
import shared.database.components.vehicle.FuelType;
import shared.request.CommandRequest;

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
    public CommandRequest execute(CommandRequest request) {
        HashMap<String, ?> args = request.getArgs();
        ((ServerEnviroment) enviroment).getDatabaseManager().add(new Vehicle(
            Vehicle.Caster.castToName.apply((String) args.get("name")), 
            new Coordinates(
                Coordinates.Caster.castToX.apply((String) args.get("x")),
                Coordinates.Caster.castToY.apply((String) args.get("y"))
            ),
            Vehicle.Caster.castToEnginePower.apply((String) args.get("enginePower")),
            Vehicle.Caster.castToNumberOfWheels.apply((String) args.get("numberOfWheels")),
            Vehicle.Caster.castToFuelConsumption.apply((String) args.get("fuelConsumption")),
            Vehicle.Caster.castToFuelType.apply((String) args.get("fuelType")),
            request.getUser().getUsername()
        ));

        request.setResponse("New Vehicle was created");
        return request;
    }
}
