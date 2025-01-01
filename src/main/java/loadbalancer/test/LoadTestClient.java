package loadbalancer.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**


 How to Run the Load Test:
 Ensure the Load Balancer is Running: Before running the test client, make sure your load balancer server (the one listening on port 9090) is up and running. You can verify this by navigating to http://localhost:9090/ in your browser or using curl.

 Run the Load Test Client:

 If using the HttpURLConnection version, compile and run the LoadTestClient class.
 If using the HttpClient version (Java 11+), compile and run the code similarly.
 Monitor the Load Balancer: Observe the server logs or console output from your load balancer. You should see the requests being routed according to the selected scheduling algorithm (ROUND_ROBIN, LEAST_CONNECTIONS, or RANDOM).

 Scaling Considerations: You can increase the delay between requests or simulate more realistic traffic patterns if needed. This will help ensure the load balancer is tested under conditions similar to production traffic.
 */
public class LoadTestClient {

    public static void main(String[] args) throws IOException {
        String urlString = "http://localhost:9090/";

        // Send 1000 requests to the load balancer
        for (int i = 1; i <= 1000; i++) {
            sendRequest(urlString, i);
            
            // Optional: Add a small delay (e.g., 50 milliseconds) between requests
            try {
                Thread.sleep(50); // Adjust the delay as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendRequest(String urlString, int requestId) {
        try {
            // Create URL object
            URL url = new URL(urlString);
            
            // Open a connection to the load balancer
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET"); // HTTP GET request
            
            // Connect to the server
            connection.connect();
            
            // Get the response code (200 indicates success)
            System.out.println(connection.getResponseMessage()) ;
            int responseCode = connection.getResponseCode();
            
            // Output the response code to console (you can also log this to a file for analysis)
            System.out.println("Request #" + requestId + " - Response Code: " + responseCode);

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            System.out.println("Request #" + requestId + " failed: " + e.getMessage());
        }
    }
}
