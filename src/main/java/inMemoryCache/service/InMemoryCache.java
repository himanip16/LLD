package inMemoryCache.service;

import inMemoryCache.evictionPolicy.EvictionPolicy;
import inMemoryCache.exception.CacheException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

public class InMemoryCache<K, V> {
    private final int capacity;
    private final Map<K, V> cacheStore; // Maps Key directly to Value
    private final EvictionPolicy<K> evictionPolicy;
    private final StampedLock lock = new StampedLock();

    public InMemoryCache(int capacity, EvictionPolicy<K> evictionPolicy) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be greater than zero.");
        this.capacity = capacity;
        this.cacheStore = new HashMap<>(capacity); // Guarded entirely by StampedLock
        this.evictionPolicy = evictionPolicy;
    }

    public V get(K key) {
        if (key == null) return null;

        // An LRU hit updates a linked list pointer, making it structurally a write operation.
        long stamp = lock.writeLock();
        try {
            V value = cacheStore.get(key);
            if (value != null) {
                evictionPolicy.keyAccessed(key);
                return value;
            }
            return null;
        } catch (Exception e) {
            throw new CacheException("Read operation failed for key: " + key, e);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        long stamp = lock.writeLock();
        try {
            if (cacheStore.containsKey(key)) {
                cacheStore.put(key, value);
                evictionPolicy.keyInserted(key); // Updates ordering
                return;
            }

            // Handle eviction cleanly before injecting the new key
            if (cacheStore.size() >= capacity) {
                K evictedKey = evictionPolicy.evictKey();
                if (evictedKey != null) {
                    cacheStore.remove(evictedKey);
                }
            }

            cacheStore.put(key, value);
            evictionPolicy.keyInserted(key);

        } catch (Exception e) {
            throw new CacheException("Write operation failed for key: " + key, e);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public void remove(K key) {
        if (key == null) return;

        long stamp = lock.writeLock();
        try {
            if (cacheStore.remove(key) != null) {
                evictionPolicy.keyRemoved(key);
            }
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}