package shared.request;

import java.io.Serializable;
import java.util.HashMap;

import shared.database.User;

public class CommandRequest implements Serializable {
    private User user;
    
    private String route;
    private HashMap<String, ?> args = new HashMap<>();

    private RequestStatus status;
    private String response;
    private Object sharedData;

    public CommandRequest(String route, HashMap<String, ?> args) {
        this.route = route;
        this.args = args;
        this.status = RequestStatus.PENDING;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRoute() {
        return route;
    }

    public Object getSharedData() {
        return sharedData;
    }

    public void setSharedData(Object sharedData) {
        this.sharedData = sharedData;
    }

    public HashMap<String, ?> getArgs() {
        return args;
    }
}
