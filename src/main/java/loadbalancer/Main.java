package loadbalancer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// Application Server Class
class ApplicationServer {
    private final String name;
    private final AtomicInteger activeConnections;

    public ApplicationServer(String name) {
        this.name = name;
        this.activeConnections = new AtomicInteger(0);
    }

    public String getName() {
        return name;
    }

    public int getActiveConnections() {
        return activeConnections.get();
    }

    public void handleRequest() {
        activeConnections.incrementAndGet();
        // Simulate request handling and then decrement connection
        // For simplicity, just simulate the request handling duration
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate request processing time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                activeConnections.decrementAndGet();
            }
        }).start();
    }
}

// Load Balancer Class
class LoadBalancer {
    private final List<ApplicationServer> servers;
    private final String schedulingAlgorithm;
    private int currentServerIndex = 0;

    public LoadBalancer(List<ApplicationServer> servers, String schedulingAlgorithm) {
        this.servers = servers;
        this.schedulingAlgorithm = schedulingAlgorithm;
    }

    public void routeRequest() {
        ApplicationServer server;
        switch (schedulingAlgorithm) {
            case "ROUND_ROBIN":
                server = roundRobin();
                break;
            case "LEAST_CONNECTIONS":
                server = leastConnections();
                break;
            case "RANDOM":
                server = randomSelection();
                break;
            default:
                throw new IllegalArgumentException("Unsupported scheduling algorithm");
        }
        System.out.println("Routing request to " + server.getName());
        server.handleRequest();
    }

    private ApplicationServer roundRobin() {
        ApplicationServer server = servers.get(currentServerIndex);
        currentServerIndex = (currentServerIndex + 1) % servers.size();
        return server;
    }

    private ApplicationServer leastConnections() {
        return servers.stream()
                .min(Comparator.comparingInt(ApplicationServer::getActiveConnections))
                .orElseThrow(() -> new IllegalStateException("No servers available"));
    }

    private ApplicationServer randomSelection() {
        Random random = new Random();
        return servers.get(random.nextInt(servers.size()));
    }
}

public class Main {
    public static void main(String[] args) {
        // Create 5 application servers
        List<ApplicationServer> servers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            servers.add(new ApplicationServer("Server-" + i));
        }

        // Instantiate Load Balancer with ROUND_ROBIN algorithm
        LoadBalancer loadBalancer = new LoadBalancer(servers, "ROUND_ROBIN");

        // Simulate 10 requests being routed through the load balancer
        for (int i = 0; i < 10; i++) {
            loadBalancer.routeRequest();
            try {
                Thread.sleep(500); // Simulate some delay between requests
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Change Load Balancer scheduling to LEAST_CONNECTIONS
        System.out.println("\nSwitching to LEAST_CONNECTIONS algorithm...");
        loadBalancer = new LoadBalancer(servers, "LEAST_CONNECTIONS");

        for (int i = 0; i < 10; i++) {
            loadBalancer.routeRequest();
            try {
                Thread.sleep(500); // Simulate some delay between requests
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Change to RANDOM scheduling algorithm
        System.out.println("\nSwitching to RANDOM algorithm...");
        loadBalancer = new LoadBalancer(servers, "RANDOM");

        for (int i = 0; i < 10; i++) {
            loadBalancer.routeRequest();
            try {
                Thread.sleep(500); // Simulate some delay between requests
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
