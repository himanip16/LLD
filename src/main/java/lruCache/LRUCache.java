package lruCache;

import lruCache.CacheNode;
import lruCache.DoublyLinkedList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe LRU Cache implementation.
 */
public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, CacheNode<K, V>> cacheMap;
    private final DoublyLinkedList<K, V> dll;

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }
        this.capacity = capacity;
        this.cacheMap = new ConcurrentHashMap<>();
        this.dll = new DoublyLinkedList<>();
    }

    /**
     * Retrieves an item from the cache.
     * Updates its status to Most Recently Used.
     */
    public synchronized V get(K key) {
        CacheNode<K, V> node = cacheMap.get(key);
        if (node == null) {
            return null;
        }
        // Move the node to the head of the DLL since it was just accessed
        dll.moveToHead(node);
        return node.value;
    }

    /**
     * Inserts or updates an item in the cache.
     * Removes the least recently used item if the cache exceeds its capacity.
     */
    public synchronized void put(K key, V value) {
        CacheNode<K, V> node = cacheMap.get(key);

        if (node != null) {
            // Update the existing node's value and move it to the head
            node.value = value;
            dll.moveToHead(node);
        } else {
            // Eviction logic if we're at capacity
            if (cacheMap.size() >= capacity) {
                CacheNode<K, V> evictedNode = dll.removeTail();
                if (evictedNode != null) {
                    cacheMap.remove(evictedNode.key);
                }
            }

            // Create a new node and add it to the head
            CacheNode<K, V> newNode = new CacheNode<>(key, value);
            dll.addAtHead(newNode);
            cacheMap.put(key, newNode);
        }
    }

    /**
     * Returns the current size of the cache.
     */
    public synchronized int size() {
        return cacheMap.size();
    }
}