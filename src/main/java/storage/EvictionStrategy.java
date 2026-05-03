package storage;

// Strategy Pattern for memory eviction
public interface EvictionStrategy<K> {
    void recordAccess(K key);

    K evict();

    void remove(K key);
}
