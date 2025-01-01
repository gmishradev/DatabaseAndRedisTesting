package redis.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDistributedLock {
    private static final String LOCK_KEY = "my_lock_key";  // Key to represent the lock
    private static final long LOCK_TIMEOUT = 10000; // Lock expiration time (milliseconds)
    private static final long WAIT_TIME = 5000; // Max time to wait for acquiring the lock (milliseconds)

    private JedisPool jedisPool;

    public RedisDistributedLock() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);

        jedisPool = new JedisPool(poolConfig, "localhost", 6379); // Redis server details
    }

    public boolean acquireLock(String lockKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < WAIT_TIME) {
                // Try to set the lock with an expiration time (SETNX)
                String result = jedis.set(lockKey, "LOCKED");
                if ("OK".equals(result)) {
                    // Lock acquired
                    return true;
                }
                // Wait before retrying
                Thread.sleep(100);
            }
            return false; // Failed to acquire lock within the wait time
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void releaseLock(String lockKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(lockKey); // Remove the lock
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RedisDistributedLock lock = new RedisDistributedLock();

        // Try to acquire lock
        if (lock.acquireLock(LOCK_KEY)) {
            System.out.println("Lock acquired, performing critical operation...");

            try {
                // Simulate some work
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Release the lock
            lock.releaseLock(LOCK_KEY);
            System.out.println("Lock released.");
        } else {
            System.out.println("Failed to acquire lock.");
        }
    }
}
