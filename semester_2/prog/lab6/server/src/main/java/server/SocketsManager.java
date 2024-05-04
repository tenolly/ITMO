package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.SerializationUtils;

import server.handlers.ServerEnviroment;
import utils.commands.Command;
import utils.commands.types.OnlyForServer;
import utils.request.CommandRequest;
import utils.request.RequestStatus;

public class SocketsManager implements AutoCloseable {
    private final static Logger LOGGER = Logger.getLogger(ServerApp.class.getName());

    private final ServerSocket serverSocket = new ServerSocket();

    private ServerEnviroment enviroment;

    public SocketsManager(String host, int port) throws IOException {
        serverSocket.bind(new InetSocketAddress(host, port));
    }

    public void setEnviroment(ServerEnviroment enviroment) {
        this.enviroment = enviroment;
    }

    public void listen() throws IOException {
        while (true) {
            Socket socket = acceptConnection();
            
            // practice requirement
            new Thread() {
                @Override
                public void run(){
                    while (true) {
                        try {
                            processRequest(socket);
                        } catch (SocketException e) {
                            LOGGER.log(Level.SEVERE, "socket disconnected", e);
                            break;
                        } catch (IOException e) {
                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                }
            }.start();
       }
    }

    private Socket acceptConnection() throws IOException {
        Socket socket = serverSocket.accept();
        
        LOGGER.log(Level.INFO, "accepted connection");

        return socket;
    }

    private void processRequest(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        int dataLength = (is.read() << 24) + (is.read() << 16) + (is.read() << 8) + (is.read() << 0);
        byte[] arr = new byte[dataLength];
        is.read(arr);

        CommandRequest request = (CommandRequest) SerializationUtils.deserialize(arr);

        LOGGER.log(Level.INFO, "received command: '" + request.getCommand() + "'");
        
        Command command = enviroment.getCommandHandler().getCommand(request.getCommand());
        if (command == null || command instanceof OnlyForServer) {
            request.setStatus(RequestStatus.REJECTED);
            request.setResponse("Command not found");
        } else if (command.getArgs().size() != 0 && request.getArgsCache() == null) {
            request.setStatus(RequestStatus.PENDING);
            request.addArgs(command.getArgs());
        } else {
            try {
                var output = command.setEnviroment(enviroment).execute(request.getArgsCache());
                request.setStatus(RequestStatus.FULLFILED);
                request.setResponse(String.join("\n", output));
                enviroment.getCommandHandler().getCommand("save").setEnviroment(enviroment).execute(null);
            } catch (Exception e) {
                request.setStatus(RequestStatus.REJECTED);
                LOGGER.log(Level.INFO, "rejection's reason: '" + e + "'");
                request.setResponse("Failed");
            }
        }
        
        os.write(SerializationUtils.serialize(request));

        LOGGER.log(Level.INFO, "sent response: '" + request.getResponse() + "'");
    }

    @Override
    public void close() throws Exception {
        serverSocket.close();
    }
}
