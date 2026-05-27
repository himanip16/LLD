package customLock;

import customLock.core.CustomLock;
import customLock.core.CustomReadWriteLock;
import customLock.impl.CustomReentrantReadWriteLock;

public class Main {
    private static int sharedResource = 0;

    public static void main(String[] args) throws InterruptedException {
        CustomReadWriteLock rwLock = new CustomReentrantReadWriteLock();
        CustomLock readLock = rwLock.readLock();
        CustomLock writeLock = rwLock.writeLock();

        // Runnable logic for concurrent reading
        Runnable readerTask = () -> {
            try {
                readLock.lock();
                System.out.println(Thread.currentThread().getName() + " acquired Read Lock. Resource value: " + sharedResource);
                Thread.sleep(100); // Simulate processing time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                readLock.unlock();
                System.out.println(Thread.currentThread().getName() + " released Read Lock.");
            }
        };

        // Runnable logic for exclusive writing
        Runnable writerTask = () -> {
            try {
                writeLock.lock();
                System.out.println(Thread.currentThread().getName() + " acquired Write Lock. Incrementing data...");
                sharedResource++;
                Thread.sleep(150); // Simulate critical section activity
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                writeLock.unlock();
                System.out.println(Thread.currentThread().getName() + " released Write Lock.");
            }
        };

        // Fire off concurrent threads
        Thread t1 = new Thread(readerTask, "Reader-1");
        Thread t2 = new Thread(readerTask, "Reader-2");
        Thread t3 = new Thread(writerTask, "Writer-1");
        Thread t4 = new Thread(readerTask, "Reader-3");
        Thread t5 = new Thread(writerTask, "Writer-2");

        t1.start();
        t2.start(); // Should log right alongside Reader-1 (Shared concurrency)
        t3.start(); // Will wait until Reader-1 and Reader-2 fully drain out
        t4.start(); // Will wait behind Writer-1's request queue to prevent starvation

        t1.join();
        t2.join();
        t3.join();
        t4.join();
    }
}