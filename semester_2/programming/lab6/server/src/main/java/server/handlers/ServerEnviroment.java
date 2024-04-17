package server.handlers;

import utils.handlers.CommandHandler;
import utils.handlers.Enviroment;

/**
* Stores and provides interaction with the CollectionHandler and CommandHandler
*/
public class ServerEnviroment extends Enviroment {
    private CollectionHandler collectionHandler;
    private CommandHandler commandHandler;

    public ServerEnviroment(CollectionHandler collectionHandler, CommandHandler commandHandler) {
        this.collectionHandler = collectionHandler;
        this.commandHandler = commandHandler;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public CollectionHandler getCollectionHandler() {
        return collectionHandler;
    }
}
