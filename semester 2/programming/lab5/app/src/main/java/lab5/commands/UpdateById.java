package lab5.commands;

import java.util.ArrayList;

import lab5.commands.core.Argument;
import lab5.commands.core.Command;
import lab5.commands.core.TerminateException;
import lab5.vehicle.CasterException;
import lab5.vehicle.Vehicle;

/**
* Updates field value of inputed object
*/
public class UpdateById extends Command {
    public UpdateById(String name, String description) {
        super(name, description);
        arguments.add(new Argument<Vehicle>("vehicle", "Input item id (integer, type !exit to terminate command): ",
            arg -> {
                if (arg == null) throw new CasterException("can't be empty");
                if (arg.equals("!exit")) throw new TerminateException();

                var vehicle = context.getEnviroment().getCollectionHandler().getById(Vehicle.Caster.castToId(arg));
                if (vehicle == null) throw new CasterException("item not found");

                return vehicle;
            }
        ));

        var mutableFields = Vehicle.getMutableFields();
        var fieldsString = new StringBuilder();

        for (String s: mutableFields) fieldsString.append("  - " + s + "\n  ");
        fieldsString.append("Input field you want to update: ");

        arguments.add(new Argument<String>("field", fieldsString.toString(),
            arg -> {
                if (arg == null) throw new CasterException("can't be empty");
                if (arg.equals("!exit")) throw new TerminateException();
                if (!mutableFields.contains(arg)) throw new CasterException("field doesn't exist");
                return arg;
            }
        ));
        arguments.add(new Argument<String>("value", "Input new value: ",
            arg -> {
                if (arg == null) throw new CasterException("can't be empty");
                if (arg.equals("!exit")) throw new TerminateException();
                return arg;
            }
        ));
    }

    @Override
    public ArrayList<String> execute() {
        ArrayList<String> output = new ArrayList<String>();

        var cache = context.readArguments();
        try {
            ((Vehicle) cache.get("vehicle")).setFieldByName(
                (String) cache.get("field"), (String) cache.get("value"));
            output.add("Field updated");
        } catch (IllegalArgumentException e) {
            output.add("You have inputed wrong value, command terminated");
        } catch (Exception e) {
            output.add("Command terminated");
        }
    
        return output;
    }
}
