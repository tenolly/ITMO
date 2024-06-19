package shared.commands;

import java.util.ArrayList;

import shared.handlers.Enviroment;
import shared.request.CommandRequest;

/**
* Class for commands.
*/
public abstract class Command {
    private String name;
    private String description;
    private ArrayList<Argument<?>> arguments = new ArrayList<Argument<?>>();
    
    protected Enviroment enviroment;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Command setEnviroment(Enviroment enviroment) {
        this.enviroment = enviroment;
        return this;
    }

    protected void initArgs(Argument<?>... args) {
        for (Argument<?> argument : args) {
            arguments.add(argument);
        }
    }

    public ArrayList<Argument<?>> getArgs() {
        return arguments;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract CommandRequest execute(CommandRequest request);
}
