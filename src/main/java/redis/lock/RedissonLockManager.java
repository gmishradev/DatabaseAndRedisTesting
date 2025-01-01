package redis.lock;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/*
1. Redisson Configuration:
We configure the Redisson client using Config. In this example, we are using a single Redis server with the URL
 "redis://localhost:6379". You can modify this if you're using a Redis cluster or other Redis setup.

2. Acquire Lock:
The acquireLock method uses RLock (Redisson's distributed lock) to acquire a lock on a resource identified by resourceId.
We use tryLock(long waitTime, long leaseTime, TimeUnit unit) to acquire the lock:
waitTime is the maximum time to wait for the lock before giving up.
leaseTime is the time for which the lock is held once acquired, after which it automatically expires.
The tryLock method returns true if the lock is acquired, otherwise false.

3. Release Lock:
The releaseLock method checks if the current thread holds the lock and releases it if true.

4. Execute Critical Section with Lock:
The executeWithLock method ensures that the given criticalSection (a Runnable) is executed only if the lock is successfully acquired.
After the critical section completes, the lock is released.

5. Shutdown Redisson:
The shutdown method shuts down the Redisson client when it's no longer needed.

Step 3: Run the Application
Before running the application, ensure that Redis is running on the specified server (localhost:6379 in this case). You can run Redis locally using Docker:

Executing critical section with lock...
Critical section execution completed.
If you try to acquire the lock again in another instance (or from another thread), it will either block or fail based on the lock configuration (e.g., tryLock will return false).

Advanced Features of Redisson Locks:

Fair Locks: Redisson supports fair locking, where the lock is granted in the order of request (FIFO).

Redisson Locks with Timeout: You can specify a timeout for the lock to automatically release after the specified time.

Reentrant Locks: Redisson supports reentrant locking, meaning the same thread can acquire the same lock multiple times.

 */

public class RedissonLockManager {

    private final RedissonClient redissonClient;

    public RedissonLockManager(String redisUrl) {
        // Create Redisson client configuration
        Config config = new Config();
        config.useSingleServer().setAddress(redisUrl);  // For single server, use a cluster config for multiple nodes
        redissonClient = Redisson.create(config);
    }

    // Acquires a distributed lock for a given resource
    public boolean acquireLock(String resourceId, long leaseTime, TimeUnit unit) {
        // Create a Redisson lock for a specific resource
        RLock lock = redissonClient.getLock(resourceId);
        
        try {
            // Try to acquire the lock within the specified lease time
            return lock.tryLock(0, leaseTime, unit); // 0 is the wait time (immediate try)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Releases a lock for a given resource
    public void releaseLock(String resourceId) {
        // Create a Redisson lock for a specific resource
        RLock lock = redissonClient.getLock(resourceId);

        // Check if the current thread holds the lock before releasing it
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    // Handles lock with a given resource and executes a critical section
    public void executeWithLock(String resourceId, Runnable criticalSection, long leaseTime, TimeUnit unit) {
        if (acquireLock(resourceId, leaseTime, unit)) {
            try {
                // Execute the critical section after acquiring the lock
                criticalSection.run();
            } finally {
                releaseLock(resourceId);  // Always release the lock after the task completes
            }
        } else {
            System.out.println("Could not acquire lock for resource: " + resourceId);
        }
    }

    // Shut down the Redisson client
    public void shutdown() {
        redissonClient.shutdown();
    }

    public static void main(String[] args) {
        // Initialize RedissonLockManager with a Redis URL
        RedissonLockManager lockManager = new RedissonLockManager("redis://localhost:6379");

        // Resource ID that needs to be locked
        String resourceId = "myResource";

        // Critical section that needs the lock
        Runnable criticalSection = () -> {
            System.out.println("Executing critical section with lock...");
            try {
                Thread.sleep(2000);  // Simulate some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Critical section execution completed.");
        };

        // Execute with a lock on the resource
        lockManager.executeWithLock(resourceId, criticalSection, 10, TimeUnit.SECONDS);

        // Shutdown Redisson client after use
        lockManager.shutdown();
    }
}
