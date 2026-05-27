package customLock.core;

public interface CustomLock {
    void lock() throws InterruptedException;
    boolean tryLock();
    void unlock();
}