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
* Returns objects that starts with inputed name
*/
public class FilterStartsWithName extends Command implements RequireAuthorization {
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
    public CommandRequest execute(CommandRequest request) {
        HashMap<String, ?> args = request.getArgs();

        var output = new ArrayList<Vehicle>();

        var elements = ((ServerEnviroment) enviroment).getDatabaseManager().getElements(
            vehicle -> vehicle.getName().startsWith(Vehicle.Caster.castToName.apply((String) args.get("name"))));

        if (elements.size() != 0) {
            elements.forEach(element -> output.add(element));
        } else {
            request.setResponse("No items found");
        }
        
        request.setSharedData(output);
        
        return request;
    }
}
