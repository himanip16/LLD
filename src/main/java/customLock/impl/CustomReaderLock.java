package customLock.impl;

import customLock.core.CustomLock;

public class CustomReaderLock implements CustomLock {
    private final ReadWriteSyncContext context;

    public CustomReaderLock(ReadWriteSyncContext context) {
        this.context = context;
    }

    @Override
    public void lock() throws InterruptedException {
        synchronized (context) {
            // Block if an active write operation is running or if a thread is waiting to write.
            while (context.getCurrentWriter() != null || context.getWriteRequests() > 0) {
                context.wait();
            }
            context.incrementReaders();
        }
    }

    @Override
    public boolean tryLock() {
        synchronized (context) {
            if (context.getCurrentWriter() == null && context.getWriteRequests() == 0) {
                context.incrementReaders();
                return true;
            }
            return false;
        }
    }

    @Override
    public void unlock() {
        synchronized (context) {
            if (context.getReaders() == 0) {
                throw new IllegalMonitorStateException("No read locks are actively held.");
            }
            context.decrementReaders();
            context.notifyAll(); // Wake up pending writers
        }
    }
}