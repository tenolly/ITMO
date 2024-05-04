package server.commands;

import java.util.ArrayList;
import java.util.HashMap;

import server.handlers.ServerEnviroment;
import utils.commands.Argument;
import utils.commands.Command;
import utils.commands.exceptions.TerminateException;
import utils.vehicle.CasterException;
import utils.vehicle.Vehicle;

/**
* Updates field value of inputed object
*/
public class UpdateById extends Command {
    public UpdateById(String name, String description) {
        super(name, description);

        var mutableFields = Vehicle.getMutableFields();
        var fieldsString = new StringBuilder();

        for (String s: mutableFields) fieldsString.append("  - " + s + "\n  ");
        fieldsString.append("Input field you want to update: ");

        initArgs(
            new Argument<Vehicle>(
                "vehicle", 
                "Input item id (integer, type !exit to terminate command): ",
                arg -> {
                    if (arg == null) throw new CasterException("can't be empty");
                    if (arg.equals("!exit")) throw new TerminateException();

                    var vehicle = ((ServerEnviroment) enviroment).getCollectionHandler().getById(Vehicle.Caster.castToId.apply(arg));
                    if (vehicle == null) throw new CasterException("item not found");

                    return vehicle;
                }
            ),
            new Argument<String>(
                "field",
                fieldsString.toString(),
                arg -> {
                    if (arg == null) throw new CasterException("can't be empty");
                    if (arg.equals("!exit")) throw new TerminateException();
                    if (!mutableFields.contains(arg)) throw new CasterException("field doesn't exist");
                    return arg;
                }
            ),
            new Argument<String>(
                "value", 
                "Input new value: ",
                arg -> {
                    if (arg == null) throw new CasterException("can't be empty");
                    if (arg.equals("!exit")) throw new TerminateException();
                    return arg;
                }
            )
        );
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        try {
            ((Vehicle) args.get("vehicle")).setFieldByName(
                (String) args.get("field"), (String) args.get("value"));
            output.add("Field updated");
        } catch (IllegalArgumentException e) {
            output.add("You have inputed wrong value, command terminated");
        } catch (Exception e) {
            output.add("Command terminated");
        }
    
        return output;
    }
}
