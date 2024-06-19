package server.managers;

import shared.caster.CasterException;
import shared.database.User;

public class AuthManager {
    private static ServerEnviroment enviroment;

    public static void setServerEnviroment(ServerEnviroment enviroment) {
        AuthManager.enviroment = enviroment;
    }
    
    public static void register(User user) {
        if (((ServerEnviroment) enviroment).getDatabaseManager().usernameExsists(user.getUsername())) 
                    throw new CasterException("user with this username already exists");

        ((ServerEnviroment) enviroment).getDatabaseManager().addUser(user);
    }

    public static boolean auth(User user) {
        return (((ServerEnviroment) enviroment).getDatabaseManager().userExsists(user));
    }
}
