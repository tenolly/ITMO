package lab5;

import java.util.ArrayList;
import java.util.Scanner;

import lab5.commands.Add;
import lab5.commands.Clear;
import lab5.commands.CountLessThanEnginePower;
import lab5.commands.ExecuteScript;
import lab5.commands.Exit;
import lab5.commands.FilterGreaterThanFuelConsumption;
import lab5.commands.FilterStartsWithName;
import lab5.commands.Help;
import lab5.commands.Info;
import lab5.commands.RemoveById;
import lab5.commands.RemoveFirst;
import lab5.commands.RemoveHead;
import lab5.commands.Save;
import lab5.commands.Show;
import lab5.commands.UpdateById;
import lab5.commands.core.Context;
import lab5.handlers.CollectionHandler;
import lab5.handlers.CommandHandler;
import lab5.handlers.Enviroment;

/**
* Main App
*/
public class App {
    public static void main(String[] args) {
        System.out.println("Type help if you are at a loss and don't know what to do ^_^");

        ArrayList<String> options = new ArrayList<String>();
        if (args.length == 0) {
            options.add("tmp.xml");
        } else {
            options.add(args[0]);
        }        

        try (Scanner scanner = new Scanner(System.in)) {
            var enviroment = new Enviroment(
                new CollectionHandler(options.get(0)),
                new CommandHandler()
                    .add(new Help("help", "outputs all commands with using explanation"))
                    .add(new Info("info", "outputs the collection information"))
                    .add(new Show("show", "outputs all collection elements"))
                    .add(new Add("add", "add a new element"))
                    .add(new Clear("clear", "clears the collection"))
                    .add(new Exit("exit", "terminate the program"))
                    .add(new Save("save", "save the collection to the file", options.get(0)))
                    .add(new RemoveFirst("remove_first", "remove first element from the collection"))
                    .add(new RemoveHead("remove_head", "return and remove first element from the collection"))
                    .add(new RemoveById("remove_by_id", "remove element from the collection by id"))
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
                    .add(new ExecuteScript("execute_script", "execute commands from file"))
            ).setScanner(scanner);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("Terminate process...");
                    enviroment.getCommandHandler().getCommand("save").setContext(new Context(enviroment)).execute();
                }
            });

            enviroment.commandListener();
        }
    }
}
