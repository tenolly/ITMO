package shared.handlers;

import shared.commands.Command;
import shared.request.CommandRequest;

/**
* Stores and provides interaction with the CollectionHandler and CommandHandler
*/
public class Enviroment {
    public String executeCommand(Command command, CommandRequest request) {
        command.setEnviroment(this);
        try {
            var res = command.execute(request);
            return res.getResponse();
        } catch (Exception e) {
            return "Failed";
        }
    }
}
