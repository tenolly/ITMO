package server;

import java.nio.charset.StandardCharsets;

import com.fastcgi.FCGIInterface;

public class Server {
    private static final String RESPONSE_TEMPLATE = "Content-Type: application/json\n" +
                                                    "Content-Length: %d\n\n%s";

    public static void listen() { 
        while(new FCGIInterface().FCGIaccept() >= 0) {
            sendJson("{\"result\": true}");
        }
    }

    private static void sendJson(String jsonDump) {
        System.out.println(String.format(RESPONSE_TEMPLATE, jsonDump.getBytes(StandardCharsets.UTF_8).length, jsonDump));
    }
}
