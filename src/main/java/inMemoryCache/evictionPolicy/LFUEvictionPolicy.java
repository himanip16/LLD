package inMemoryCache.evictionPolicy;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * LFU (Least Frequently Used) Implementation using frequency-bucketed LinkedHashSets.
 * Guarantees true O(1) eviction execution processing speeds.
 */
public class LFUEvictionPolicy<K> implements EvictionPolicy<K> {
    private final Map<K, Integer> keyToFrequency = new HashMap<>();
    private final Map<Integer, LinkedHashSet<K>> frequencyToKeys = new HashMap<>();
    private int minFrequency = 0;

    @Override
    public void keyAccessed(K key) {
        Integer currentFreq = keyToFrequency.get(key);
        if (currentFreq == null) return; // Guard clause against uninserted keys

        int newFreq = currentFreq + 1;
        keyToFrequency.put(key, newFreq);

        // Move key out of the old frequency bucket
        LinkedHashSet<K> oldBucket = frequencyToKeys.get(currentFreq);
        if (oldBucket != null) {
            oldBucket.remove(key);
            if (oldBucket.isEmpty()) {
                frequencyToKeys.remove(currentFreq);
                // If the emptied bucket was the global minimum frequency, update the pointer
                if (currentFreq == minFrequency) {
                    minFrequency++;
                }
            }
        }

        // Move key into upgraded frequency bucket
        frequencyToKeys.computeIfAbsent(newFreq, k -> new LinkedHashSet<>()).add(key);
    }

    @Override
    public void keyInserted(K key) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");

        // FIX: If key already exists, route to keyAccessed to prevent state corruption
        if (keyToFrequency.containsKey(key)) {
            keyAccessed(key);
            return;
        }

        // Fresh insertion
        keyToFrequency.put(key, 1);
        frequencyToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFrequency = 1; // Safely set to 1 because a brand new item has a frequency of 1
    }

    @Override
    public void keyRemoved(K key) {
        Integer freq = keyToFrequency.remove(key);
        if (freq != null) {
            LinkedHashSet<K> bucket = frequencyToKeys.get(freq);
            if (bucket != null) {
                bucket.remove(key);
                if (bucket.isEmpty()) {
                    frequencyToKeys.remove(freq);
                    // Note: If you remove the minFrequency item, your parent Cache's put()
                    // method will immediately insert a new item right after, resetting minFrequency to 1.
                }
            }
        }
    }

    @Override
    public K evictKey() {
        if (keyToFrequency.isEmpty()) return null;

        LinkedHashSet<K> keysWithMinFreq = frequencyToKeys.get(minFrequency);
        if (keysWithMinFreq == null || keysWithMinFreq.isEmpty()) return null;

        // LinkedHashSet iterator guarantees FIFO order (oldest inserted key in this frequency tie)
        K leastFrequentlyUsedKey = keysWithMinFreq.iterator().next();

        keysWithMinFreq.remove(leastFrequentlyUsedKey);
        if (keysWithMinFreq.isEmpty()) {
            frequencyToKeys.remove(minFrequency);
        }
        keyToFrequency.remove(leastFrequentlyUsedKey);

        return leastFrequentlyUsedKey;
    }
}