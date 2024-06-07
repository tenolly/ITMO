package server.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import utils.caster.CasterUtil;
import utils.caster.CasterException;
import utils.commands.Argument;
import utils.commands.Command;
import utils.database.User;
import server.managers.ServerEnviroment;

public class Registration extends Command implements Serializable {
    public Registration(String name, String description) {
        super(name, description);
        this.initArgs(
            new Argument<String>(
                "username", 
                "Input username (type !exit to terminate command): ", 
                CasterUtil.castToString
            ),
            new Argument<String>(
                "password", 
                "Input password (type !exit to terminate command): ", 
                CasterUtil.castToString
            )
        );
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        String username = (String) args.get("username");
        String hashedPassword = (String) args.get("password");

        if (((ServerEnviroment) enviroment).getDatabaseManager().usernameExsists(username)) 
            throw new CasterException("user with this username already exists");
        
        var user = new User(username, hashedPassword);
        ((ServerEnviroment) enviroment).getDatabaseManager().addUser(user);
        output.add("User added");

        return output;
    }
}
