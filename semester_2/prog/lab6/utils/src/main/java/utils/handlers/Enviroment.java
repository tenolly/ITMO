package utils.handlers;

import java.util.HashMap;

import utils.commands.Command;

/**
* Stores and provides interaction with the CollectionHandler and CommandHandler
*/
public class Enviroment {
    public String executeCommand(Command command, HashMap<String, ?> args) {
        command.setEnviroment(this);
        try {
            var output = command.execute(args);
            return String.join("\n", output);
        } catch (Exception e) {
            return "Failed";
        }
    }
}
