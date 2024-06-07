package client.commands;

import java.util.ArrayList;
import java.util.HashMap;

import utils.commands.Command;
import utils.commands.types.RequireAuthorization;
import client.managers.ClientEnviroment;

public class UnsetUser extends Command implements RequireAuthorization {
    public UnsetUser(String name, String description) {
        super(name, description);
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        var output = new ArrayList<String>();

        ((ClientEnviroment) enviroment).setUser(null);
        output.add("User unset");

        return output;
    }
}
