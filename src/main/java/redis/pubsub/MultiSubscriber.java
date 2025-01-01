package redis.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;



/*


Explanation:
Publisher (RedisPublisher):
Establishes a connection to Redis.

Publishes messages to the notifications channel using the publish() method.
Subscriber (RedisSubscriber):
Establishes a connection to Redis.
Subscribes to the notifications channel using the subscribe() method.
The onMessage method is called every time a message is published to the notifications channel.
3. Running the Example
Step 1: Start the subscriber first (RedisSubscriber). It will wait and listen for incoming messages on the specified channel (notifications).

Step 2: Then, run the publisher (RedisPublisher). It will send a message to the notifications channel.

Expected Output:

When you run the RedisSubscriber:

css
Copy code
Connected to Redis
Subscribed to channel notifications
When you run the RedisPublisher:

css
Copy code
Connected to Redis
The subscriber will print the messages received:

csharp
Copy code
Received message from channel notifications: Hello from Redis Publisher!
Received message from channel notifications: Another message from the publisher.
4. Multi-Subscriber Example
You can run multiple subscribers in different threads or processes, and they will all receive messages published to the channel. Redis ensures that all the subscribers to a given channel get the messages.

For example, you can create several subscribers that run concurrently and print messages when they are received.

5. Use Cases for Redis as a Message Broker
Event-Driven Architecture: Using Redis' pub/sub, different parts of your application can be notified when specific events occur (e.g., user registration, order placed).

Real-Time Notifications: Redis pub/sub can be used to send real-time notifications to users across multiple clients. For instance, a chat application can use Redis to broadcast messages to all connected clients.

Decoupled Communication: Pub/Sub allows different parts of your system to communicate without tightly coupling them. For example, a backend service can publish an event (e.g., a new user registration), and multiple services can react to it independently.

6. Troubleshooting
Connection issues: Make sure Redis is running on the specified host and port (localhost:6379 by default).
Channel not receiving messages: Ensure that your subscriber is actively listening to the correct channel and that the publisher is publishing messages to the same channel.
Multiple subscribers: If you start multiple subscriber instances, each will receive a copy of the messages published to the subscribed channel.
7. Scaling and Persistence
Redis' Pub/Sub model is great for messaging in real-time, but remember that messages are not stored. If a subscriber is disconnected, it misses the messages.
For persistence, Redis supports Streams (introduced in Redis 5.0) and Lists which can be used to implement message queues with persistence and acknowledgment, which can be more appropriate for reliable message delivery.
8. Advanced Redis Messaging (Streams)
If you want more advanced features like message persistence, acknowledgment, and message replay, consider using Redis Streams instead of Pub/Sub. Redis Streams allow you to persist messages and replay them for new consumers.

Let me know if you want an example with Redis Streams!
 */

public class MultiSubscriber {

    public static void main(String[] args) {
        Runnable subscriberTask = () -> {
            Jedis jedis = new Jedis("localhost", 6379);
            jedis.subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    System.out.println(Thread.currentThread().getName() + " - Received message: " + message);
                }
            }, "notifications");
        };

        // Start multiple subscribers
        new Thread(subscriberTask, "Subscriber-1").start();
        new Thread(subscriberTask, "Subscriber-2").start();
    }
}
