package client.commands;

import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.google.common.base.Function;

import client.managers.ClientEnviroment;
import utils.commands.Argument;
import utils.commands.Command;
import utils.commands.types.RequireAuthorization;

/**
* Executes commands from a file
*/
public class ExecuteScript extends Command implements RequireAuthorization {
    public ExecuteScript(String name, String description) {
        super(name, description);
        initArgs(new Argument<String>("filename", "Input path to file: ", (Function<String, String> & Serializable) arg -> arg));
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        var filename = (String) args.get("filename");
        ArrayList<String> listOfCommands = ((ClientEnviroment) enviroment).getListOfLines(
            filename, new ArrayList<String>(), new ArrayList<String>(), (string) -> string.equals("execute_script")
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (String line: listOfCommands) {
            try {
                baos.write(line.getBytes());
                baos.write("\n".getBytes());
            } catch (IOException e) {
                output.add("Something goes wrong");
                return output;
            }
        }

        ((ClientEnviroment) enviroment).addScanner(new Scanner(new ByteArrayInputStream(baos.toByteArray())));

        return output;
    }
}