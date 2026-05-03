package storage;

public class Chunk {
    private final long chunkIndex;
    private final int chunkSize;
    private byte[] data = null;
    private boolean isLoaded = false;

    public Chunk(long chunkIndex, int chunkSize) {
        this.chunkIndex = chunkIndex;
        this.chunkSize = chunkSize;
    }

    /**
     * Thread-safely guarantees data is fetched from network only once.
     */
    public synchronized byte[] load(String uri, StorageClient client, long fileSize) {
        if (isLoaded) {
            return data;
        }

        long startOffset = chunkIndex * chunkSize;
        long endOffset = Math.min(startOffset + chunkSize, fileSize);
        int lengthToFetch = (int) (endOffset - startOffset);

        data = new byte[lengthToFetch];
        client.fetch(uri, startOffset, lengthToFetch, data);

        isLoaded = true;
        return data;
    }

    /**
     * Erases the byte array to free memory.
     */
    public synchronized void invalidate() {
        this.data = null;
        this.isLoaded = false;
    }
}