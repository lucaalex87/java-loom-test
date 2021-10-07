import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Test {

    public static Double count = 0.0;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        HttpContext context = server.createContext("/test");
        context.setHandler(Test::handleRequest);
        server.start();
        System.out.println("Server started on port 8500");
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String responseString = "Request count: "+Test.count++;
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
                exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                try {
                    Thread.sleep(6000);
                    exchange.sendResponseHeaders(200, responseString.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(responseString.getBytes());
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
