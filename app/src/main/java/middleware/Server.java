package middleware;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


public class Server {

    private static final int SERVER_PORT = 8100;
    private static final String SERVER_HOSTNAME = "localhost";

    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        HttpServer server = HttpServer.create(
            new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
            0
        );

        // server.createContext("/recipe-builder", new RecipeBuilderHandler());

        server.setExecutor(threadPoolExecutor);
        server.start();
    }
}


// class RecipeBuilderHandler implements HttpHandler {
//     public void handle(HttpExchange httpExchange) throws IOException {
//         String response = "Hello World!";
//         httpExchange.sendResponseHeaders(200, response.length());
//         OutputStream outStream = httpExchange.getResponseBody();
//         outStream.write(response.getBytes());
//         outStream.close();
//     }
// }