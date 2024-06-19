package client.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandUtil {
    public static HashMap<String, Object> makeArgs(String commandName, ArrayList<String> listArgs) throws IndexOutOfBoundsException  {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("command", commandName);

        switch (commandName) {
            case "add":
                args.put("name", listArgs.get(0));
                args.put("x", listArgs.get(1));
                args.put("y", listArgs.get(2));
                args.put("enginePower", listArgs.get(3));
                args.put("numberOfWheels", listArgs.get(4));
                args.put("fuelConsumption", listArgs.get(5));
                args.put("fuelType", listArgs.get(6));
                break;
            case "count_less_than_engine_power":
                args.put("enginePower", listArgs.get(0));
                break;
            case "filter_greater_than_fuel_consumption":
                args.put("fuelConsumption", listArgs.get(0));
                break;
            case "filter_starts_with_name":
                args.put("name", listArgs.get(0));
                break;
            case "remove_by_id":
                args.put("id", listArgs.get(0));
                break;
            case "remove_lower":
                args.put("id", listArgs.get(0));
                break;
            case "update":
                args.put("vehicle_id", listArgs.get(0));
                args.put("field", listArgs.get(1));
                args.put("value", listArgs.get(2));
            default:
                break;
        }

        return args;
    }
}
