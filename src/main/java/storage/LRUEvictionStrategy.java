package storage;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUEvictionStrategy<K> implements EvictionStrategy<K> {
    private final int capacity;
    private final LinkedHashMap<K, Boolean> accessOrderMap;

    public LRUEvictionStrategy(int capacity) {
        this.capacity = capacity;
        // true activates access-order instead of insertion-order
        this.accessOrderMap = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, Boolean> eldest) {
                return size() > capacity;
            }
        };
    }

    @Override
    public synchronized void recordAccess(K key) {
        accessOrderMap.put(key, Boolean.TRUE);
    }

    @Override
    public synchronized K evict() {
        if (accessOrderMap.isEmpty()) {
            return null;
        }
        // First entry is the eldest (least recently accessed)
        K eldestKey = accessOrderMap.keySet().iterator().next();
        accessOrderMap.remove(eldestKey);
        return eldestKey;
    }

    @Override
    public synchronized void remove(K key) {
        accessOrderMap.remove(key);
    }
}