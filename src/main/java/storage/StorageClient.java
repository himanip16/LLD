package storage;

// Provided low-level storage interface
public interface StorageClient {
    long getFileSize(String uri);
    void fetch(String uri, long offset, int length, byte[] buffer);
}

