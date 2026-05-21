package inMemoryCache.model;

// --- CORE CACHE WRAPPER ---

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CacheNode<K, V> {
    private final K key;
    private V value;
    private int frequency;

    public CacheNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.frequency = 1; // Starts with a frequency of 1 on instantiation
    }

    public void incrementFrequency() { this.frequency++; }
}
