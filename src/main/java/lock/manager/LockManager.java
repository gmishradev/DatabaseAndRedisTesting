package lock.manager;

import java.util.concurrent.locks.*;
import java.util.concurrent.*;
import java.util.*;

/*

Key Functions:
acquireLock(String resource, Thread thread): This method checks whether a resource is locked or not. If it's not, it locks the resource and returns true. Otherwise, it adds the thread to the waiting queue.
releaseLock(String resource, Thread thread): This method unlocks a resource and removes the corresponding thread from the waiting queue.
isLocked(String resource): This checks whether a resource is currently locked.
 */

/*

Open-Source Libraries for Lock Management in Distributed Systems
There are several open-source libraries and systems available for distributed lock management. Some of the most commonly used ones are:

Apache ZooKeeper:

ZooKeeper is a popular distributed coordination service that provides high-level primitives for building distributed systems, including distributed locks. It can be used to coordinate locks across multiple nodes in a distributed system.
ZooKeeper Official Site
Redisson (Redis):

Redisson is a client for Redis that provides distributed locks and other concurrency utilities. It allows distributed locks using Redis as the backend.
Redisson GitHub Repository
It provides various features, such as lock retries, timeouts, and blocking lock options.
etcd:

etcd is a distributed key-value store used for coordinating distributed systems. It provides a lock API that helps in managing distributed locks.
etcd Official Site
Consul:

Consul by HashiCorp is another tool that provides a service discovery and coordination system. It also includes features for distributed locks and leader election.
Consul Official Site
Curator (Apache ZooKeeper-based):

Curator is a Java client for ZooKeeper that simplifies working with ZooKeeper. It includes recipes for distributed locks, leader election, and other coordination tasks.
Curator GitHub Repository
Hazelcast:

Hazelcast provides a distributed in-memory data grid and includes support for distributed locks. It is an in-memory platform for data and computation, offering a lock manager for distributed systems.
Hazelcast Official Site
etcd Client for Java:

Provides distributed coordination and locking capabilities for Java applications using etcd.
etcd Java Client

 */

public class LockManager {
    
    private final Map<String, Lock> resourceLocks = new ConcurrentHashMap<>();
    private final Map<String, Queue<Thread>> waitingQueue = new ConcurrentHashMap<>();
    
    // Lock acquisition method
    public synchronized boolean acquireLock(String resource, Thread thread) {
        resourceLocks.putIfAbsent(resource, new ReentrantLock());
        Lock lock = resourceLocks.get(resource);
        
        // If the lock is already held by another thread, add this thread to the waiting queue
        if (lock.tryLock()) {
            return true; // Lock acquired
        } else {
            // Add the thread to the waiting queue
            waitingQueue.putIfAbsent(resource, new LinkedList<>());
            waitingQueue.get(resource).offer(thread);
            return false; // Lock not acquired
        }
    }
    
    // Lock release method
    public synchronized void releaseLock(String resource, Thread thread) {
        Lock lock = resourceLocks.get(resource);
        if (lock != null) {
            lock.unlock();
            waitingQueue.get(resource).poll(); // Remove the thread from the queue
        }
    }
    
    // Check if the resource is locked
    public boolean isLocked(String resource) {
        Lock lock = resourceLocks.get(resource);
        return lock != null && lock.tryLock();
    }
}
