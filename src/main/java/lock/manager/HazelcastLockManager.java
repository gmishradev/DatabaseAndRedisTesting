/*
package lock.manager;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.concurrent.TimeUnit;


*/
/*

1. Hazelcast Configuration:
A Hazelcast instance is created with a simple Config. We set the cluster name to "myCluster",
which allows multiple Hazelcast nodes to join the same cluster.
The Hazelcast.newHazelcastInstance(config) method starts the local Hazelcast instance.
 If you're running multiple instances, they will automatically form a cluster.

2. Distributed Lock (ILock):
We use ILock, a distributed lock that Hazelcast provides, to manage locks for resources.
The acquireLock method tries to acquire the lock on a resource with a specific leaseTime
(time the lock will be held) and TimeUnit (such as seconds, milliseconds).
The method returns true if the lock is acquired, and false otherwise.
The tryLock(long waitTime, long leaseTime, TimeUnit unit) method allows the client to specify
 how long it should wait to acquire the lock and how long it can hold the lock.

3. Releasing the Lock:
The releaseLock method releases the lock after the critical section has been executed.
 It ensures that the lock is only released if the current thread holds it.

4. Executing Critical Section:
The executeWithLock method ensures that the critical section (provided as a Runnable)
 is only executed after acquiring the lock. It automatically releases the lock after the critical section completes.

5. Shutdown:
After use, the shutdown method is called to shut down the Hazelcast instance, which is important to release resources
 and close connections to the cluster.

Step 3: Run the Application
To run the application, you need to have Hazelcast running. By default, Hazelcast will automatically create a cluster if there are multiple Hazelcast nodes (i.e., multiple instances of your application running). You can run the same application on different machines or on the same machine in different processes for testing.

Advanced Features of Hazelcast Locks:
Fair Locks: Hazelcast supports fair locking, where the lock is granted to threads in the order in which they requested it (FIFO).

To use a fair lock:
ILock lock = hazelcastInstance.getLock(resourceId, true); // true for fair lock
Lock Timeout: Hazelcast supports setting timeouts on acquiring the lock.

If the lock is not acquired within the specified time, the thread will fail to acquire the lock.

lock.tryLock(10, TimeUnit.SECONDS); // Tries to acquire the lock for 10 seconds
Reentrant Locks: Hazelcast's ILock is reentrant,
meaning that the same thread can acquire the lock multiple times without getting blocked,
 provided it releases the lock the same number of times.
 *//*


public class HazelcastLockManager {

    private final HazelcastInstance hazelcastInstance;

    public HazelcastLockManager(String clusterName) {
        // Configure Hazelcast instance
        Config config = new Config();
        config.setClusterName(clusterName); // Specify the cluster name
        hazelcastInstance = Hazelcast.newHazelcastInstance(config); // Create Hazelcast instance
    }

    // Acquires a distributed lock for a given resource
    public boolean acquireLock(String resourceId, long leaseTime, TimeUnit unit) {
        ILock lock = hazelcastInstance.getLock(resourceId); // Get a lock for the resource
        try {
            // Try to acquire the lock within the specified lease time
            return lock.tryLock(0, leaseTime, unit); // 0 is the wait time (immediate try)
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Releases a lock for a given resource
    public void releaseLock(String resourceId) {
        ILock lock = hazelcastInstance.getLock(resourceId); // Get a lock for the resource
        // Check if the current thread holds the lock before releasing it
        if (lock.isLockedByCurrentThread()) {
            lock.unlock();
        }
    }

    // Executes a critical section with a distributed lock
    public void executeWithLock(String resourceId, Runnable criticalSection, long leaseTime, TimeUnit unit) {
        if (acquireLock(resourceId, leaseTime, unit)) {
            try {
                // Execute the critical section after acquiring the lock
                criticalSection.run();
            } finally {
                releaseLock(resourceId); // Always release the lock after the task completes
            }
        } else {
            System.out.println("Could not acquire lock for resource: " + resourceId);
        }
    }

    // Shut down the Hazelcast instance
    public void shutdown() {
        hazelcastInstance.shutdown();
    }

    public static void main(String[] args) {
        // Initialize HazelcastLockManager with the desired cluster name
        HazelcastLockManager lockManager = new HazelcastLockManager("myCluster");

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

        // Shutdown Hazelcast client after use
        lockManager.shutdown();
    }
}
*/
