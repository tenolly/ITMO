package lab5.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import lab5.commands.core.Argument;
import lab5.commands.core.Command;

/**
* Executes commands from a file
*/
public class ExecuteScript extends Command {
    public ExecuteScript(String name, String description) {
        super(name, description);
        arguments.add(new Argument<String>("filename", "Input path to file: ", arg -> arg));
    }

    @Override
    public ArrayList<String> execute() {
        ArrayList<String> output = new ArrayList<String>();

        var cache = context.readArguments();
        var filename = (String) cache.get("filename");
        try (Scanner scanner = new Scanner(new File(filename))) {
            context.getEnviroment().setTempScanner(scanner, filename);
        } catch (FileNotFoundException e) {
            output.add("\nError: file not found");
        }

        return output;
    }
}
