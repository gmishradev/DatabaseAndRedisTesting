package redis.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


/**
 Subscriber (RedisSubscriber):
 Establishes a connection to Redis.
 Subscribes to the notifications channel using the subscribe() method.
 The onMessage method is called every time a message is published to the notifications channel.

 */

public class RedisSubscriber {

    public static void main(String[] args) {
        // Create a new Jedis instance
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println("Connected to Redis");

        // Subscribe to the "notifications" channel
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                // This method will be called when a message is received on the subscribed channel
                System.out.println("Received message from channel " + channel + ": " + message);
            }

            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                System.out.println("Subscribed to channel " + channel);
            }

            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                System.out.println("Unsubscribed from channel " + channel);
            }

            @Override
            public void onPMessage(String pattern, String channel, String message) {
                // If you're using pattern-based subscriptions, this method will be called
            }

            @Override
            public void onPSubscribe(String pattern, int subscribedChannels) {
                // If you're using pattern-based subscriptions, this method will be called
            }

            @Override
            public void onPUnsubscribe(String pattern, int subscribedChannels) {
                // If you're using pattern-based subscriptions, this method will be called
            }
        }, "notifications"); // Subscribe to the "notifications" channel

        // The subscriber will keep running indefinitely, so you can test it by publishing messages.
    }
}
