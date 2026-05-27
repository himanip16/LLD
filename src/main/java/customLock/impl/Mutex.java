package customLock.impl;

import customLock.core.CustomLock;

public class Mutex implements CustomLock {
    private Thread owner = null;

    @Override
    public synchronized void lock() throws InterruptedException {
        // While the lock is held by another thread, suspend execution safely
        while (owner != null) {
            wait();
        }
        owner = Thread.currentThread();
    }

    @Override
    public synchronized boolean tryLock() {
        if (owner == null) {
            owner = Thread.currentThread();
            return true;
        }
        return false;
    }

    @Override
    public synchronized void unlock() {
        if (owner != Thread.currentThread()) {
            throw new IllegalMonitorStateException("Calling thread does not hold the lock.");
        }
        owner = null;
        notifyAll(); // Wake up any pending threads waiting for acquisition
    }
}