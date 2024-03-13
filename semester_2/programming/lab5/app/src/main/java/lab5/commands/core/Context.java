package lab5.commands.core;

import java.util.HashMap;
import java.util.ArrayList;

import lab5.handlers.Enviroment;
import lab5.vehicle.CasterException;

/**
* Class for command interaction with the environment
*/
public class Context {
    private Enviroment enviroment;
    private ArrayList<Argument<?>> args;

    public Context(Enviroment enviroment) {
        this.enviroment = enviroment;
    }

    public Enviroment getEnviroment() {
        return this.enviroment;
    }

    public void setArgs(ArrayList<Argument<?>> args) {
        this.args = args;
    }

    public HashMap<String, Object> readArguments() {
        var cache = new HashMap<String, Object>();
        if (args != null) {
            var scanner = enviroment.getScanner();
            for (int i = 0; i < args.size(); ++i) {
                var argument = args.get(i);
                System.out.print("  " + argument.getDescription());
                try {
                    argument.setValue(scanner.nextLine().trim());
                    cache.put(argument.getName(), argument.getValue());
                } catch (TerminateException e) {
                    break;
                } catch (CasterException e) {
                    System.out.println("    Invalid value: " + e.getMessage() + ", try again!");
                    --i;
                } catch (IllegalArgumentException e) {
                    System.out.println("    Invalid value: wrong type or empty value, try again!");
                    --i;
                }
            }
        }
        return cache;
    }

    public HashMap<String, Object> readArgument(String alias) throws IllegalArgumentException {
        var cache = new HashMap<String, Object>();
        
        var scanner = enviroment.getScanner();
        Argument<?> requestedArgument = null;
        for (int i = 0; i < args.size(); ++i) {
            var argument = args.get(i);
            if (args.get(i).getName() == alias) {
                requestedArgument = argument;
                break;
            }
        }

        if (requestedArgument == null) throw new IllegalArgumentException("Not found argument with this alias");
        
        System.out.print("  " + requestedArgument.getDescription());
        try {
            requestedArgument.setValue(scanner.nextLine().trim());
            cache.put(requestedArgument.getName(), requestedArgument.getValue());
        } catch (CasterException e) {
                System.out.println("    Invalid value: " + e.getMessage() + ", try again!");
        } catch (IllegalArgumentException e) {
                System.out.println("    Invalid value: wrong type or empty value, try again!");
        }

        return cache;
    }
}
