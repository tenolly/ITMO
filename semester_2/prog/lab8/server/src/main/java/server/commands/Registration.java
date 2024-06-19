package server.commands;

import java.io.Serializable;

import shared.caster.CasterUtil;
import shared.caster.CasterException;
import shared.commands.Argument;
import shared.commands.Command;
import shared.request.CommandRequest;
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
    public CommandRequest execute(CommandRequest request) {
        if (((ServerEnviroment) enviroment).getDatabaseManager().usernameExsists(request.getUser().getUsername())) 
            throw new CasterException("user with this username already exists");
        
        ((ServerEnviroment) enviroment).getDatabaseManager().addUser(request.getUser());

        request.setResponse("User added");

        return request;
    }
}
