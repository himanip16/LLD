package inMemoryCache.evictionPolicy;

public interface EvictionPolicy<K> {
    void keyAccessed(K key);

    void keyInserted(K key);

    void keyRemoved(K key);

    K evictKey();
}
