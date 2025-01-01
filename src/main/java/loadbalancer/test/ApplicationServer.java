package loadbalancer.test;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.SQLOutput;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ApplicationServer {
    private final String name;
    private final AtomicInteger activeConnections;
    private final int port;
    private final  Random random;

    public ApplicationServer(String name, int port) throws IOException {
        this.name = name;
        this.port = port;
        this.activeConnections = new AtomicInteger(0);
        this.random = new Random(100);

        // Start an HTTP server on the specified port
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", this::handleRequest);
        System.out.println("Starting " + name + " on port " + port);
        server.start();
    }

    public void handleRequest(HttpExchange exchange) throws IOException {
        activeConnections.incrementAndGet();

        // Simulate request handling (send response)
        String response = "Request handled by " + name;
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

        // Simulate request processing time
        new Thread(() -> {
            try {
                Thread.sleep(Math.abs(random.nextInt())); // Simulate request processing time
                System.out.println("Govind Request is processed by server " + response);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                activeConnections.decrementAndGet();
            }
        }).start();
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public int getActiveConnections() {
        return activeConnections.get();
    }
}
