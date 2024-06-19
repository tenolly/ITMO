package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.SerializationUtils;

import server.managers.AuthManager;
import server.managers.ServerEnviroment;
import shared.caster.CasterException;
import shared.commands.Command;
import shared.commands.types.OnlyForServer;
import shared.commands.types.RequireAuthorization;
import shared.database.User;
import shared.request.CommandRequest;
import shared.request.RequestStatus;

public class Server implements AutoCloseable {
    private final static Logger LOGGER = Logger.getLogger(ServerApp.class.getName());

    private final ServerSocket serverSocket = new ServerSocket();

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private ServerEnviroment enviroment;

    public Server(String host, int port) throws IOException {
        serverSocket.bind(new InetSocketAddress(host, port));
    }

    public void setEnviroment(ServerEnviroment enviroment) {
        this.enviroment = enviroment;
    }

    public void listen() throws IOException {
        while (true) {
            Socket socket = acceptConnection();

            threadPool.submit(new Task(socket));
       }
    }

    private Socket acceptConnection() throws IOException {
        Socket socket = serverSocket.accept();
        
        LOGGER.log(Level.INFO, "accepted connection");

        return socket;
    }

    private void processRequest(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        os.flush();

        int dataLength = (is.read() << 24) + (is.read() << 16) + (is.read() << 8) + (is.read() << 0);

        byte[] arr = new byte[dataLength];
        is.read(arr);

        ByteArrayInputStream bais = new ByteArrayInputStream(arr);
        ObjectInputStream tmpIn = new ObjectInputStream(bais);
        CommandRequest request = (CommandRequest) tmpIn.readObject();

        LOGGER.log(Level.INFO, "Received route: " + request.getRoute() + " with args " + request.getArgs().toString());

        if (request.getRoute().equals("cmd")) {
            Command command = enviroment.getCommandHandler().getCommand((String) request.getArgs().get("command"));
            if (command == null || command instanceof OnlyForServer) {
                request.setStatus(RequestStatus.REJECTED);
                request.setResponse("Command not found");
            } else if (command instanceof RequireAuthorization && request.getUser() == null) {
                request.setStatus(RequestStatus.REJECTED);
                request.setResponse("This command require to be authorized");
            } else if (command instanceof RequireAuthorization && !this.enviroment.getDatabaseManager().userExsists(request.getUser())) {
                request.setStatus(RequestStatus.REJECTED);
                request.setResponse("User doesn't exists");
            } else {
                try {
                    request = command.setEnviroment(enviroment).execute(request);
                    request.setStatus(RequestStatus.FULLFILED);
                } catch (Exception e) {
                    request.setStatus(RequestStatus.REJECTED);
                    LOGGER.log(Level.INFO, "Rejection's reason: '" + e.getMessage() + "'");
                    request.setResponse("Failed");
                }
            }
        } else if (request.getRoute().equals("auth")) {
            User user = request.getUser();
            if (request.getArgs().get("command").equals("register")) {
                try {
                    if (user == null) {
                        request.setStatus(RequestStatus.REJECTED);
                        request.setResponse("User already exists");
                    } else {
                        AuthManager.register(user);
                        request.setStatus(RequestStatus.FULLFILED);
                        request.setResponse("Done");
                    }
                } catch (CasterException e) {
                    request.setStatus(RequestStatus.REJECTED);
                    request.setResponse("User already exists");
                }
            } else if (request.getArgs().get("command").equals("verify")) {
                if (AuthManager.auth(user)) {
                    request.setStatus(RequestStatus.FULLFILED);
                    request.setResponse("Success");
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    request.setResponse("Incorrect data");
                }
            }
        } else {
            request.setStatus(RequestStatus.REJECTED);
            request.setResponse("Route doesn't exist");
        }

        os.write(SerializationUtils.serialize(request));

        LOGGER.log(Level.INFO, "Sent response: '" + request.getResponse() + "'");
    }
    

    @Override
    public void close() throws Exception {
        serverSocket.close();
    }

    class Task implements Runnable {
        private Socket socket;

        public Task(Socket socket) {
            super();
            this.socket = socket;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    processRequest(socket);
                } catch (SocketException e) {
                    LOGGER.log(Level.SEVERE, "socket disconnected", e);
                    break;
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    break;
                } catch (ClassNotFoundException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    break;
                }
            }
        }
        
    }
}
