package inMemoryCache;

import inMemoryCache.evictionPolicy.LFUEvictionPolicy;
import inMemoryCache.service.InMemoryCache;

public class InMemoryCacheMain {

    public static void main(String[] args) {
        System.out.println("=== Initializing LFU Cache System Sandbox (Capacity: 3) ===\n");

        // 1. Instantiate the cache with an LFU eviction strategy
        InMemoryCache<String, String> cache = new InMemoryCache<>(3, new LFUEvictionPolicy<>());

        // 2. Populate the cache up to its max capacity limit
        System.out.println("-> Inserting keys: A, B, C");
        cache.put("A", "Alpha Document Data");
        cache.put("B", "Bravo Video Asset File");
        cache.put("C", "Charlie Config Matrix");

        // 3. Simulate uneven read patterns to adjust access frequencies
        System.out.println("-> Accessing 'A' twice, and 'B' once...");
        cache.get("A");
        cache.get("A");
        cache.get("B");

        /*
         * Current Frequency Breakdown:
         * Key 'C' -> Frequency: 1 (Initial insertion value)
         * Key 'B' -> Frequency: 2 (Inserted + Accessed 1x)
         * Key 'A' -> Frequency: 3 (Inserted + Accessed 2x)
         */

        // 4. Insert a new element, forcing the eviction engine to trigger
        System.out.println("\n-> Inserting key 'D' (This should trigger the eviction of 'C')...");
        cache.put("D", "Delta Kernel Object");

        // 5. Run final assertions to verify the structural integrity of the cache
        System.out.println("\n--- Final Cache Verification Check ---");
        System.out.println("Key A Content: " + cache.get("A")); // Expected: "Alpha Document Data"
        System.out.println("Key B Content: " + cache.get("B")); // Expected: "Bravo Video Asset File"
        System.out.println("Key C Content: " + cache.get("C")); // Expected: null (Successfully evicted)
        System.out.println("Key D Content: " + cache.get("D")); // Expected: "Delta Kernel Object"
    }
}

