package server;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import server.commands.Add;
import server.commands.Clear;
import server.commands.CountLessThanEnginePower;
import server.commands.FilterGreaterThanFuelConsumption;
import server.commands.FilterStartsWithName;
import server.commands.Help;
import server.commands.Info;
import server.commands.Registration;
import server.commands.RemoveById;
import server.commands.RemoveFirst;
import server.commands.RemoveHead;
import server.commands.RemoveLower;
import server.commands.Show;
import server.commands.UpdateById;
import server.managers.DatabaseManager;
import server.managers.ServerEnviroment;
import utils.handlers.CommandHandler;

public class ServerApp {
    private final static Logger LOGGER = Logger.getLogger(ServerApp.class.getName());
    static {System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s [%1$tF %1$tT]: %5$s%6$s%n");}

    public static final String HOST = "localhost";
    public static final int PORT = 2000;

    public static void main(String[] args) {
        ServerEnviroment enviroment = new ServerEnviroment(
            new DatabaseManager(),
            new CommandHandler()
                .add(new Registration("register", "registrate new user"))
                .add(new Help("help", "outputs all server commands with using explanation"))
                .add(new Info("info", "outputs the collection information"))
                .add(new Show("show", "outputs all collection elements"))
                .add(new Add("add", "add a new element"))
                .add(new Clear("clear", "clears the collection"))
                .add(new RemoveFirst("remove_first", "remove first element from the collection"))
                .add(new RemoveHead("remove_head", "return and remove first element from the collection"))
                .add(new RemoveById("remove_by_id", "remove element from the collection by id"))
                .add(new RemoveLower("remove_lower", "remove elements from the collection lower than parameter"))
                .add(new CountLessThanEnginePower(
                    "count_less_than_engine_power", 
                    "outputs the number of items thats enginePower field value is less than the inputed value")
                )
                .add(new FilterGreaterThanFuelConsumption(
                    "filter_greater_than_fuel_consumption", 
                    "outputs the items thats fuelConsumption field value is more than the inputed value")
                )
                .add(new FilterStartsWithName(
                    "filter_starts_with_name", 
                    "outputs the items that name field value starts with the inputed value")
                )
                .add(new UpdateById("update", "update item, input !exit to terminate"))
        );

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOGGER.log(Level.INFO,"Server was terminated");
            }
        });

        try (Server socketsManager = new Server(HOST, PORT)) {
            FileHandler fileHandler = new FileHandler("server_logs.log");
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);

            LOGGER.log(Level.INFO, "server started at " + HOST + ":" + PORT);
            socketsManager.setEnviroment(enviroment);
            socketsManager.listen();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        
    }
    
}
