package storage;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CachedFile {
    private static final int DEFAULT_CHUNK_SIZE = 64 * 1024; // 64 KB
    private static final int MAX_CACHED_CHUNKS = 1024;       // ~64 MB max RAM usage per file

    private final String uri;
    private final StorageClient client;
    private final long fileSize;
    private final int chunkSize;

    private final ConcurrentMap<Long, Chunk> cache = new ConcurrentHashMap<>();
    private final EvictionStrategy<Long> evictionStrategy;

    public CachedFile(String uri, StorageClient client) {
        this(uri, client, DEFAULT_CHUNK_SIZE, MAX_CACHED_CHUNKS);
    }

    public CachedFile(String uri, StorageClient client, int chunkSize, int maxCachedChunks) {
        this.uri = uri;
        this.client = client;
        this.chunkSize = chunkSize;
        this.fileSize = client.getFileSize(uri);
        this.evictionStrategy = new LRUEvictionStrategy<>(maxCachedChunks);
    }

    /**
     * Thread-safe chunk retrieval with proactive LRU eviction management.
     */
    private Chunk getOrCreateChunk(long chunkIndex) {
        Chunk chunk = cache.computeIfAbsent(chunkIndex, k -> {
            // Evict if cache exceeds max constraints
            synchronized (evictionStrategy) {
                Long evictedKey = evictionStrategy.evict();
                if (evictedKey != null) {
                    Chunk evictedChunk = cache.remove(evictedKey);
                    if (evictedChunk != null) {
                        evictedChunk.invalidate();
                    }
                }
            }
            return new Chunk(chunkIndex, chunkSize);
        });

        // Track chunk access for cache priority
        evictionStrategy.recordAccess(chunkIndex);
        return chunk;
    }

    /**
     * Reads arbitrary ranges of bytes from the cached or remote storage.
     */
    public int read(long offset, int length, byte[] targetBuffer) {
        if (offset < 0 || offset >= fileSize || length <= 0) {
            return 0;
        }

        long endOffset = Math.min(offset + length, fileSize);
        int totalBytesRead = 0;

        long currentOffset = offset;
        while (currentOffset < endOffset) {
            long chunkIndex = currentOffset / chunkSize;
            int chunkStartOffset = (int) (chunkIndex * chunkSize);

            int offsetInChunk = (int) (currentOffset - chunkStartOffset);
            int bytesFromChunk = Math.min(chunkSize - offsetInChunk, (int) (endOffset - currentOffset));

            Chunk chunk = getOrCreateChunk(chunkIndex);
            byte[] cachedData = chunk.load(uri, client, fileSize);

            // Copy chunk segment to caller's buffer
            System.arraycopy(cachedData, offsetInChunk, targetBuffer, totalBytesRead, bytesFromChunk);

            totalBytesRead += bytesFromChunk;
            currentOffset += bytesFromChunk;
        }

        return totalBytesRead;
    }

    public long getFileSize() {
        return fileSize;
    }
}