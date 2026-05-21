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
        int currentFreq = keyToFrequency.get(key);
        int newFreq = currentFreq + 1;
        keyToFrequency.put(key, newFreq);

        // Move key out of current frequency bucket
        frequencyToKeys.get(currentFreq).remove(key);
        if (frequencyToKeys.get(currentFreq).isEmpty()) {
            frequencyToKeys.remove(currentFreq);
            if (currentFreq == minFrequency) {
                minFrequency++;
            }
        }

        // Move key into upgraded frequency bucket
        frequencyToKeys.computeIfAbsent(newFreq, k -> new LinkedHashSet<>()).add(key);
    }

    @Override
    public void keyInserted(K key) {
        keyToFrequency.put(key, 1);
        frequencyToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFrequency = 1;
    }

    @Override
    public void keyRemoved(K key) {
        Integer freq = keyToFrequency.remove(key);
        if (freq != null) {
            frequencyToKeys.get(freq).remove(key);
            if (frequencyToKeys.get(freq).isEmpty()) {
                frequencyToKeys.remove(freq);
            }
        }
    }

    @Override
    public K evictKey() {
        if (frequencyToKeys.isEmpty()) return null;

        LinkedHashSet<K> keysWithMinFreq = frequencyToKeys.get(minFrequency);
        K leastFrequentlyUsedKey = keysWithMinFreq.iterator().next(); // Gets the oldest element (FIFO within frequency tie)

        keysWithMinFreq.remove(leastFrequentlyUsedKey);
        if (keysWithMinFreq.isEmpty()) {
            frequencyToKeys.remove(minFrequency);
        }
        keyToFrequency.remove(leastFrequentlyUsedKey);

        return leastFrequentlyUsedKey;
    }
}
