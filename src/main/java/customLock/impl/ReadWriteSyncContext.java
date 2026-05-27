package customLock.impl;

/**
 * Shared state context for coordinating Read and Write locks.
 * Eliminates the need for inner classes by acting as the shared monitor.
 */
public class ReadWriteSyncContext {
    private int readers = 0;
    private int writeRequests = 0;
    private Thread currentWriter = null;

    public synchronized int getReaders() { return readers; }
    public synchronized void incrementReaders() { readers++; }
    public synchronized void decrementReaders() { readers--; }

    public synchronized int getWriteRequests() { return writeRequests; }
    public synchronized void incrementWriteRequests() { writeRequests++; }
    public synchronized void decrementWriteRequests() { writeRequests--; }

    public synchronized Thread getCurrentWriter() { return currentWriter; }
    public synchronized void setCurrentWriter(Thread writer) { this.currentWriter = writer; }
}