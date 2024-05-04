package client;

import java.io.FileNotFoundException;
import java.io.IOException;
public class ClientApp {
    public static void main(String[] args) {
        String HOST = "localhost";
        int PORT = 2000;

        String LOG_FILE = "errors.log";

        try {
            System.out.println("Type client-help (to print client-side commands) or server-help (to print server-side commands)");
            System.out.println("\nConnecting to the server (" + HOST + ":" + PORT + ")");
            Client client = new Client(HOST, PORT, LOG_FILE);
            client.start();
        } catch (FileNotFoundException e) {
            System.out.println("File " + LOG_FILE + " not found");
        } catch (IOException e) {
            System.out.println("Fatal error");
        }
    }
}
