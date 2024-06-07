package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.managers.ServerEnviroment;
import utils.commands.Argument;
import utils.commands.Command;
import utils.commands.types.RequireAuthorization;
import utils.database.Vehicle;

/**
* Counts objects that less than inputed by engine power value
*/
public class CountLessThanEnginePower extends Command implements RequireAuthorization {
    public CountLessThanEnginePower(String name, String description) {
        super(name, description);
        initArgs(
            new Argument<Float>(
                "enginePower", 
                "Input engine power " + Vehicle.Validator.enginePowerHumanIntelligibleConditions + ": ", Vehicle.Caster.castToEnginePower
            )
        );
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        var output = new ArrayList<String>();

        int count = ((ServerEnviroment) enviroment).getDatabaseManager().countElements(
            vehicle -> vehicle.getEnginePower() < (Float) args.get("enginePower"));

        output.add("Found " + count + " items");
        return output;
    }
}
