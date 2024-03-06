package lab5.commands.core;

import java.util.ArrayList;

/**
* Class for commands.
*/
public abstract class Command {
    private String name;
    private String description;

    protected Context context;
    protected ArrayList<Argument<?>> arguments = new ArrayList<Argument<?>>();

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Command setContext(Context context) {
        context.setArgs(arguments);
        this.context = context;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public abstract ArrayList<String> execute();
}
