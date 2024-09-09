import com.fastcgi.FCGIInterface;

public class App {
    public static void main (String args[]) {  
        int count = 0;

        while(new FCGIInterface().FCGIaccept() >= 0) {
            count++;
            System.out.println( "Content-type: text/html\n\n" +
                                "<html>" +
                                "<head><TITLE>FastCGI-Hello Java stdio</TITLE></head>" +
                                "<body>" +
                                "<H3>FastCGI-HelloJava stdio</H3>" +
                                "request number " + count + " running on host " + System.getProperty("SERVER_NAME") +
                                "</body>" +
                                "</html>"); 
        }
    }
}
