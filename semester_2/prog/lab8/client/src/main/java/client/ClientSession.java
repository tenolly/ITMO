package client;

import java.util.HashMap;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.ConnectException;

import client.managers.ClientEnviroment;
import client.managers.DataManager;
import shared.database.User;
import shared.request.CommandRequest;

public class ClientSession {
    private static User user;
    private static String currentLocale;
    private static DataManager dataManager;

    private static ClientEnviroment enviroment;

    public static void start(String host, int port) throws IOException, FileNotFoundException {
        ClientSession.dataManager = new DataManager(host, port);
        try {
            ClientSession.dataManager.connect();
        } catch (ConnectException e) {
            System.out.println("The server is not available, try later");
        }
    }

    public static ClientEnviroment getClientEnviroment() {
        return enviroment;
    }

    public static void setClientEnviroment(ClientEnviroment enviroment) {
        ClientSession.enviroment = enviroment;
    }

    public static void setCurrentLocale(String currentLocale) {
        ClientSession.currentLocale = currentLocale;
    }

    public static String getCurrentLocale() {
        return currentLocale;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        ClientSession.user = user;
    }

    public static void unsetUser() {
        ClientSession.user = null;
    }

    public static CommandRequest makeRequest(String route, HashMap<String, Object> args) throws IOException {
        if (!dataManager.isAvailable()) dataManager.reconnect();
        CommandRequest request = new CommandRequest(route, args);
        request.setUser(user);
        return dataManager.sendRequest(request);
    }
}
