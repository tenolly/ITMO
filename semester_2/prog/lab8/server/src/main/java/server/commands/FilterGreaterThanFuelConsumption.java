package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.managers.ServerEnviroment;
import shared.commands.Argument;
import shared.commands.Command;
import shared.commands.types.RequireAuthorization;
import shared.database.Vehicle;
import shared.request.CommandRequest;

/**
* Returns objects that greater than inputed by fuel consumption value
*/
public class FilterGreaterThanFuelConsumption extends Command implements RequireAuthorization {
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
    public CommandRequest execute(CommandRequest request) {
        HashMap<String, ?> args = request.getArgs();

        var output = new ArrayList<Vehicle>();

        var elements = ((ServerEnviroment) enviroment).getDatabaseManager().getElements(
            vehicle -> vehicle.getFuelConsumption() > Vehicle.Caster.castToFuelConsumption.apply((String) args.get("fuelConsumption")));

        if (elements.size() != 0) {
            elements.forEach(element -> output.add(element));
        } else {
            request.setResponse("No items found");
        }

        request.setSharedData(output);
        
        return request;
    }
}
