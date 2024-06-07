package client.commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Function;

import utils.caster.CasterException;
import utils.commands.Argument;
import utils.commands.Command;
import utils.commands.exceptions.TerminateException;
import utils.database.User;
import client.managers.ClientEnviroment;

public class SetUser extends Command {
    public SetUser(String name, String description) {
        super(name, description);
        this.initArgs(
            new Argument<String>(
                "username", 
                "Input username (type !exit to terminate command): ", 
                (Function<String, String> & Serializable) arg -> {
                    if (arg == null) throw new CasterException("can't be empty");
                    if (arg.isEmpty()) throw new CasterException("can't be empty");
                    if (arg.equals("!exit")) throw new TerminateException();
                    return arg;
                }
            ),
            new Argument<String>(
                "password", 
                "Input password (type !exit to terminate command): ", 
                (Function<String, String> & Serializable) arg -> {
                    if (arg == null) throw new CasterException("can't be empty");
                    if (arg.isEmpty()) throw new CasterException("can't be empty");
                    if (arg.equals("!exit")) throw new TerminateException();
                    return arg;
                }
            )
        );
    }

    @Override
    public ArrayList<String> execute(HashMap<String, ?> args) {
        ArrayList<String> output = new ArrayList<String>();

        var user = new User((String) args.get("username"), (String) args.get("password"));
        ((ClientEnviroment) enviroment).setUser(user);
        output.add("User set");

        return output;
    }
}
