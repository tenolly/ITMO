package server.managers;

import utils.handlers.CommandHandler;
import utils.handlers.Enviroment;

/**
* Stores and provides interaction with the databaseManager and CommandHandler
*/
public class ServerEnviroment extends Enviroment {
    private DatabaseManager databaseManager;
    private CommandHandler commandHandler;

    public ServerEnviroment(DatabaseManager databaseManager, CommandHandler commandHandler) {
        this.databaseManager = databaseManager;
        this.commandHandler = commandHandler;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
