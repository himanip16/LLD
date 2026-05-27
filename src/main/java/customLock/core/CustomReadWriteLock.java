package customLock.core;

public interface CustomReadWriteLock {
    CustomLock readLock();
    CustomLock writeLock();
}