package loadbalancer.test;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        // Create 5 application servers on different ports (e.g., 8081, 8082, 8083, 8084, 8085)
        List<ApplicationServer> servers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            servers.add(new ApplicationServer("Server-" + i, 8080 + i));
        }

        // Instantiate Load Balancer with ROUND_ROBIN algorithm
        LoadBalancer loadBalancer = new LoadBalancer(servers, "ROUND_ROBIN");

        // Create HTTP server on port 9090 (the load balancer's port)
        HttpServer loadBalancerServer = HttpServer.create(new InetSocketAddress(9090), 0);
        loadBalancerServer.createContext("/", exchange -> loadBalancer.routeRequest(exchange));

        // Start the Load Balancer HTTP server
        System.out.println("Starting Load Balancer on port 9090...");
        loadBalancerServer.start();
    }
}
