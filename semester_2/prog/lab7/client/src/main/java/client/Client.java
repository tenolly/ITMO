package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

import client.commands.ExecuteScript;
import client.commands.Exit;
import client.commands.Help;
import client.commands.SetUser;
import client.commands.UnsetUser;
import client.managers.ClientEnviroment;
import client.managers.DataManager;
import utils.caster.CasterException;
import utils.commands.Command;
import utils.commands.exceptions.TerminateException;
import utils.commands.types.CallOnBothSides;
import utils.commands.types.RequireAuthorization;
import utils.handlers.CommandHandler;
import utils.request.CommandRequest;
import utils.request.RequestStatus;

public class Client {
    private DataManager dataManager;
    private ClientEnviroment enviroment;

    private String logFile;
    private PrintStream errorStream;

    public Client(String host, int port, String logFile) throws IOException, FileNotFoundException {
        this.dataManager = new DataManager(host, port);
        try {
            this.dataManager.connect();
        } catch (ConnectException e) {
            System.out.println("The server is not available, try later");
        }

        this.enviroment = new ClientEnviroment(
            new CommandHandler()
                .add(new SetUser("set_user", "sets user"))
                .add(new UnsetUser("unset_user", "unsets user"))
                .add(new Exit("exit", "terminate the application"))
                .add(new Help("help", "outputs all client commands with using explanation"))
                .add(new ExecuteScript("execute_script", "execute commands from file"))
        );
        this.enviroment.addScanner(new Scanner(System.in));
        
        this.logFile = logFile;
        this.errorStream = new PrintStream(new File(logFile));
    }

    public void start() {
        try {
            while (this.enviroment.getScannersStackSize() != 0) {
                if (!this.enviroment.getScanner().hasNext()) {
                    this.enviroment.popScanner();
                    continue;
                }

                var newLine = this.enviroment.getScanner().nextLine().trim();
                if (newLine.isEmpty()) continue;

                CommandRequest request = new CommandRequest(newLine);
                request.setUser(this.enviroment.getUser());

                Command command = enviroment.getCommandHandler().getCommand(newLine);

                if (command instanceof RequireAuthorization && this.enviroment.getUser() == null) {
                    System.out.println("This command require to have set user");
                    continue;
                }

                if (command != null) {
                    if (command.getArgs().size() != 0) {
                        request.addArgs(command.getArgs());
                        try {
                            request = readArgs(request);
                        } catch (NoSuchElementException e) {
                            System.out.println("Argument not found");
                            continue;
                        }
                    };
                    System.out.println(enviroment.executeCommand(command, request.getArgsCache()));
                } 

                if (command == null || command instanceof CallOnBothSides) {
                    try {
                        processRequest(request);
                    } catch (IOException e) {
                        System.out.println("The server is not available, try later");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(errorStream);
            System.out.println("Something goes wrong, see " + logFile + " for more information");
        }
    }

    public void processRequest(CommandRequest request) throws IOException {
        if (!dataManager.is_available()) dataManager.reconnect();
        CommandRequest response = dataManager.sendRequest(request);
        processResponce(response);
    }

    public void processResponce(CommandRequest response) throws IOException {
        if (response.getStatus().equals(RequestStatus.FULLFILED) || response.getStatus().equals(RequestStatus.REJECTED)) {
            System.out.println(response.getResponse());
        } else if (response.getStatus().equals(RequestStatus.PENDING)) {
            try {
                processRequest(readArgs(response));
            } catch (NoSuchElementException e) {
                System.out.println("Argument not found");
            }
        }
    }

    public CommandRequest readArgs(CommandRequest response) throws NoSuchElementException {
        var cmdArgs = response.getArgs();
        var cache = new HashMap<String, Object>();
        for (int i = 0; i < cmdArgs.size(); ++i) {
            var argument = cmdArgs.get(i);
            System.out.print("  " + argument.getDescription());
            try {
                argument.setValue(this.enviroment.getScanner().nextLine().trim());
                cache.put(argument.getName(), argument.getValue());
            } catch (TerminateException e) {
                break;
            } catch (CasterException e) {
                System.out.println("    Invalid value: " + e.getMessage() + ", try again!");
                --i;
            } catch (IllegalArgumentException e) {
                System.out.println("    Invalid value: wrong type or empty value, try again!");
                --i;
            }
        }
        response.setArgsCache(cache);
        return response;
    }
}
