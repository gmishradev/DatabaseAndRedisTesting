package redis.pubsub;

import redis.clients.jedis.Jedis;


/*

Using Redis as a message broker involves leveraging  Redis' pub/sub (publish/subscribe)
capabilities. Redis' pub/sub system allows clients to publish messages to channels and other
 clients to subscribe to those channels to receive messages in real-time.
 This can be useful for many use cases like event-driven systems, message queues, notifications, etc.

Here's an example of how you can implement Redis as a message broker in
Java using Jedis for both publishing and subscribing.


Publisher (RedisPublisher):
Establishes a connection to Redis.
Publishes messages to the notifications channel using to publish() method.


 */

public class RedisPublisher {

    public static void main(String[] args) {
        // Connect to Redis
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("Connected to Redis");

        // Publish a message to the "notifications" channel
        jedis.publish("notifications", "Hello from Redis Publisher!");

        // You can publish more messages if needed
        jedis.publish("notifications", "Another message from the publisher.");

        // Close the connection
        jedis.close();
    }
}
