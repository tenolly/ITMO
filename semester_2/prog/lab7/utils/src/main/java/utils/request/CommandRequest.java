package utils.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import utils.commands.Argument;
import utils.database.User;

public class CommandRequest implements Serializable {
    private User user;
    
    private RequestStatus status;
    private String response;

    private String command;
    private ArrayList<Argument<?>> args = new ArrayList<Argument<?>>();
    private HashMap<String, Object> argsCache;

    public CommandRequest(String command) {
        this.command = command;
        this.status = RequestStatus.PENDING;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommand() {
        return command;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public ArrayList<Argument<?>> getArgs() {
        return args;
    }

    public void addArg(Argument<?> argument) {
        args.add(argument);
    }

    public void addArgs(ArrayList<Argument<?>> arguments) {
        args.addAll(arguments);
    }

    public HashMap<String, Object> getArgsCache() {
        return argsCache;
    }

    public void setArgsCache(HashMap<String, Object> cache) {
        this.argsCache = cache;
    }
}
