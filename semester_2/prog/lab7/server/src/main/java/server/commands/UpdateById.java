package server.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import utils.caster.CasterException;
import utils.caster.CasterUtil;
import utils.commands.Argument;
import utils.commands.Command;
import utils.commands.types.RequireAuthorization;
import utils.database.Vehicle;
import server.managers.ServerEnviroment;

/**
* Updates field value of inputed object
*/
public class UpdateById extends Command implements Serializable, RequireAuthorization {
    private Set<String> mutableFields = Vehicle.getMutableFields();

    public UpdateById(String name, String description) {
        super(name, description);

        var fieldsString = new StringBuilder();

        for (String s: mutableFields) fieldsString.append("  - " + s + "\n  ");
        fieldsString.append("Input field you want to update: ");

        initArgs(
            new Argument<Long>(
                "vehicle_id", 
                "Input item id (integer, type !exit to terminate command): ",
                Vehicle.Caster.castToId
            ),
            new Argument<String>(
                "field",
                fieldsString.toString(),
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
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        try {
            var vehicle = ((ServerEnviroment) enviroment).getDatabaseManager().getById((Long) args.get("vehicle_id"));
            if (vehicle == null) throw new CasterException("item not found");
            if (!vehicle.getUsername().equals((String) args.get("username"))) throw new CasterException("isn't allowed");
            if (!mutableFields.contains((String) args.get("field"))) throw new CasterException("field doesn't exist");
            
            ((ServerEnviroment) enviroment).getDatabaseManager().update(vehicle, (String) args.get("field"), (String) args.get("value"));
            output.add("Field updated");
        } catch (CasterException e) {
            output.add(e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            output.add("You have inputed wrong value, command terminated");
        } catch (Exception e) {
            output.add("Command terminated");
        }
    
        return output;
    }
}
