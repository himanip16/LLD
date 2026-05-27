package customLock.impl;

import customLock.core.CustomLock;
import customLock.core.CustomReadWriteLock;

public class CustomReentrantReadWriteLock implements CustomReadWriteLock {
    private final CustomLock readerLock;
    private final CustomLock writerLock;

    public CustomReentrantReadWriteLock() {
        // Create the single shared state monitor
        ReadWriteSyncContext context = new ReadWriteSyncContext();

        // Pass it explicitly to independent classes
        this.readerLock = new CustomReaderLock(context);
        this.writerLock = new CustomWriterLock(context);
    }

    @Override
    public CustomLock readLock() {
        return readerLock;
    }

    @Override
    public CustomLock writeLock() {
        return writerLock;
    }
}