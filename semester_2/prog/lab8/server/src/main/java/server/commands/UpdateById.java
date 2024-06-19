package server.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import shared.caster.CasterException;
import shared.caster.CasterUtil;
import shared.commands.Argument;
import shared.commands.Command;
import shared.commands.types.RequireAuthorization;
import shared.database.Vehicle;
import shared.request.CommandRequest;
import server.managers.ServerEnviroment;

/**
* Updates field value of inputed object
*/
public class UpdateById extends Command implements Serializable, RequireAuthorization {
    public UpdateById(String name, String description) {
        super(name, description);
        initArgs(
            new Argument<Long>(
                "vehicle_id", 
                "Input item id (integer, type !exit to terminate command): ",
                Vehicle.Caster.castToId
            ),
            new Argument<String>(
                "field",
                "Input field you want to update: ",
                CasterUtil.castToString
            ),
            new Argument<String>(
                "value", 
                "Input new value: ",
                CasterUtil.castToString
            )
        );
    }

    @Override
    public CommandRequest execute(CommandRequest request) {
        HashMap<String, ?> args = request.getArgs();
        ArrayList<String> output = new ArrayList<String>();

        var vehicle = ((ServerEnviroment) enviroment).getDatabaseManager().getById(Long.parseLong((String) args.get("vehicle_id")));
        if (vehicle == null) throw new CasterException("item not found");
        if (!vehicle.getUsername().equals(request.getUser().getUsername())) throw new CasterException("isn't allowed");
        
        ((ServerEnviroment) enviroment).getDatabaseManager().update(vehicle, (String) args.get("field"), (String) args.get("value"));
        output.add("Field updated");
    
        request.setResponse(String.join("\n", output));

        return request;
    }
}
