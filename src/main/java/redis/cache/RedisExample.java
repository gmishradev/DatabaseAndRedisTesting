package redis.cache;

import redis.clients.jedis.Jedis;

public class RedisExample {
    public static void main(String[] args) {
        // Connect to Redis server
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("Connection to server successfully");

        // Create
        System.out.println("Creating data...");
        jedis.set("user:1000:name", "John Doe");
        jedis.set("user:1000:email", "john.doe@example.com");
        System.out.println("Data inserted successfully");

        // Read
        System.out.println("Reading data...");
        String name = jedis.get("user:1000:name");
        String email = jedis.get("user:1000:email");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);

        // Update (To update, Redis doesn't have an explicit update operation, just set again)
        System.out.println("Updating data...");
        jedis.set("user:1000:name", "Jane Doe");
        jedis.set("user:1000:email", "jane.doe@example.com");
        System.out.println("Data updated successfully");

        // Read updated data
        name = jedis.get("user:1000:name");
        email = jedis.get("user:1000:email");
        System.out.println("Updated Name: " + name);
        System.out.println("Updated Email: " + email);

        // Delete
        System.out.println("Deleting data...");
        jedis.del("user:1000:name");
        jedis.del("user:1000:email");
        System.out.println("Data deleted successfully");

        // Verify deletion
        name = jedis.get("user:1000:name");
        email = jedis.get("user:1000:email");
        System.out.println("Deleted Name: " + name); // Should print null
        System.out.println("Deleted Email: " + email); // Should print null

        // Close the connection
        jedis.close();
    }
}
