package customLock.impl;

import customLock.core.CustomLock;

public class CustomWriterLock implements CustomLock {
    private final ReadWriteSyncContext context;

    public CustomWriterLock(ReadWriteSyncContext context) {
        this.context = context;
    }

    @Override
    public void lock() throws InterruptedException {
        synchronized (context) {
            context.incrementWriteRequests();
            try {
                // Block if there are active readers or if another thread holds the write lock
                while (context.getReaders() > 0 || context.getCurrentWriter() != null) {
                    context.wait();
                }
            } catch (InterruptedException e) {
                context.decrementWriteRequests(); // Rollback intent on interruption
                throw e;
            }
            context.decrementWriteRequests();
            context.setCurrentWriter(Thread.currentThread());
        }
    }

    @Override
    public boolean tryLock() {
        synchronized (context) {
            if (context.getReaders() == 0 && context.getCurrentWriter() == null) {
                context.setCurrentWriter(Thread.currentThread());
                return true;
            }
            return false;
        }
    }

    @Override
    public void unlock() {
        synchronized (context) {
            if (context.getCurrentWriter() != Thread.currentThread()) {
                throw new IllegalMonitorStateException("Calling thread does not hold the write lock.");
            }
            context.setCurrentWriter(null);
            context.notifyAll(); // Wake up pending readers or other writers
        }
    }
}