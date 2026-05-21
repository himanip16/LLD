package inMemoryCache.service;

import inMemoryCache.evictionPolicy.EvictionPolicy;
import inMemoryCache.exception.CacheException;
import inMemoryCache.model.CacheNode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class InMemoryCache<K, V> {
    private final int capacity;
    private final Map<K, CacheNode<K, V>> cacheStore;
    private final EvictionPolicy<K> evictionPolicy;

    // StampedLock supports optimistic reading and explicit safe upgrading
    private final StampedLock lock = new StampedLock();

    public InMemoryCache(int capacity, EvictionPolicy<K> evictionPolicy) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than zero.");
        this.capacity = capacity;
        this.cacheStore = new ConcurrentHashMap<>(capacity);
        this.evictionPolicy = evictionPolicy;
    }

    public V get(K key) {
        // Try an optimistic read first (completely lock-free)
        long stamp = lock.tryOptimisticRead();
        CacheNode<K, V> node = cacheStore.get(key);

        // If another thread modified the structure during our read, validate() returns false
        if (!lock.validate(stamp)) {
            // Fall back to a traditional read lock
            stamp = lock.readLock();
            try {
                node = cacheStore.get(key);
            } finally {
                lock.unlockRead(stamp);
            }
        }

        if (node == null) return null;

        // Safely upgrade to a write lock to update eviction tracking metadata
        long writeStamp = lock.writeLock();
        try {
            // Double-check that the node wasn't evicted while we were waiting for the lock
            if (cacheStore.containsKey(key)) {
                node.incrementFrequency();
                evictionPolicy.keyAccessed(key);
                return node.getValue();
            }
            return null;
        } catch (Exception e) {
            throw new CacheException("Failed to update cache access metadata for key: " + key, e);
        } finally {
            lock.unlockWrite(writeStamp);
        }
    }

    public void put(K key, V value) {
        long stamp = lock.writeLock();
        try {
            if (cacheStore.containsKey(key)) {
                CacheNode<K, V> existingNode = cacheStore.get(key);
                existingNode.setValue(value);
                existingNode.incrementFrequency();
                evictionPolicy.keyAccessed(key);
                return;
            }

            // Handle eviction if we're at capacity
            if (cacheStore.size() >= capacity) {
                try {
                    K evictedKey = evictionPolicy.evictKey();
                    if (evictedKey != null) {
                        cacheStore.remove(evictedKey);
                    }
                } catch (Exception e) {
                    // Prevent eviction tracking issues from crashing the main write operation
                    System.err.println("[WARN] Eviction policy failed to run cleanly: " + e.getMessage());
                }
            }

            CacheNode<K, V> newNode = new CacheNode<>(key, value);
            cacheStore.put(key, newNode);
            evictionPolicy.keyInserted(key);

        } catch (Exception e) {
            throw new CacheException("Write operation failed for key: " + key, e);
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}