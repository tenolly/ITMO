package server.commands;

import java.util.HashMap;

import server.managers.ServerEnviroment;
import shared.commands.Argument;
import shared.commands.Command;
import shared.commands.types.RequireAuthorization;
import shared.database.Vehicle;
import shared.request.CommandRequest;

/**
* Counts objects that less than inputed by engine power value
*/
public class CountLessThanEnginePower extends Command implements RequireAuthorization {
    public CountLessThanEnginePower(String name, String description) {
        super(name, description);
        initArgs(
            new Argument<Float>(
                "enginePower", 
                "Input engine power " + Vehicle.Validator.enginePowerHumanIntelligibleConditions + ": ", 
                Vehicle.Caster.castToEnginePower
            )
        );
    }

    @Override
    public CommandRequest execute(CommandRequest request) {
        HashMap<String, ?> args = request.getArgs();

        int count = ((ServerEnviroment) enviroment).getDatabaseManager().countElements(
            vehicle -> vehicle.getEnginePower() < Vehicle.Caster.castToEnginePower.apply((String) args.get("enginePower")));

        request.setResponse(Integer.toString(count));
        return request;
    }
}
