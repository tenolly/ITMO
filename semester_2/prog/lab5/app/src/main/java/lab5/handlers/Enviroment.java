package lab5.handlers;

import java.util.ArrayList;
import java.util.Scanner;

import lab5.commands.core.Context;

/**
* Stores and provides interaction with the CollectionHandler and CommandHandler
*/
public class Enviroment {
    private CollectionHandler collectionHandler;
    private CommandHandler commandHandler;

    private ArrayList<Scanner> scanners = new ArrayList<Scanner>();
    private ArrayList<String> stack = new ArrayList<String>();

    public Enviroment(CollectionHandler collectionHandler, CommandHandler commandHandler) {
        this.collectionHandler = collectionHandler;
        this.commandHandler = commandHandler;
    }

    public Enviroment commandListener() {
        var scanner = getScanner();
        while (scanner.hasNext()) {
            var newLine = scanner.nextLine().trim();
            if (newLine.isEmpty()) continue;
            
            var command = commandHandler.getCommand(newLine);
            if (command == null) System.out.println("  Command not found");
            else {
                try {
                    var output = command.setContext(new Context(this)).execute();
                    System.out.println(String.join("\n", output));
                } catch (Exception e) {
                    System.out.println("Failed");
                }
            }
        }
        return this;
    }

    public Scanner getScanner() {
        return scanners.get(scanners.size() - 1);
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public CollectionHandler getCollectionHandler() {
        return collectionHandler;
    }

    public Enviroment setScanner(Scanner scanner) {
        this.scanners.add(scanner);
        return this;
    }

    public void setTempScanner(Scanner tempScanner, String filename) {
        if (stack.contains(filename)) throw new IllegalArgumentException("Recursion found");
        stack.add(filename);

        this.scanners.add(tempScanner);
        commandListener();

        this.scanners.remove(scanners.size() - 1);
        stack.remove(filename);
    }
}
