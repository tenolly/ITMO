import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.fastcgi.FCGIInterface;

public class App {
    private static final String RESPONSE_TEMPLATE = "Content-Type: application/json\n" +
                                                    "Content-Length: %d\n\n%s";

    public static void main (String args[]) { 
        while(new FCGIInterface().FCGIaccept() >= 0) {
            try {
                HashMap<String, String> params = Params.parse(FCGIInterface.request.params.getProperty("QUERY_STRING"));
                int x = Integer.parseInt(params.get("x"));
                float y = Float.parseFloat(params.get("y"));
                float r = Float.parseFloat(params.get("r"));

                if (Validator.validateX(x) && Validator.validateY(y) && Validator.validateR(r)) {
                    sendJson(String.format("{\"result\": %b}", Checker.hit(x, y, r)));
                } else {
                    sendJson("{\"error\": \"invalid data\"}");
                }
            } catch (NumberFormatException e) {
                sendJson("{\"error\": \"wrong query param type\"}");
            } catch (NullPointerException e) {
                sendJson(String.format("{\"error\": \"missed necessary query param\"}"));
            } catch (Exception e) {
                sendJson(String.format("{\"error\": %s}", e.toString()));
            }
        }
    }

    private static void sendJson(String jsonDump) {
        System.out.println(String.format(RESPONSE_TEMPLATE, jsonDump.getBytes(StandardCharsets.UTF_8).length, jsonDump));
    }
}