package rateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        RateLimiterService service = new RateLimiterService();
        User freeUser = new User("user_123", UserTier.FREE);

        // Simultaneous execution simulator using a thread pool [00:30:45]
        ExecutorService executor = Executors.newFixedThreadPool(20);

        System.out.println("--- Triggering 20 Concurrent Requests for FREE Tier ---");
        for (int i = 0; i < 20; i++) {
            executor.submit(() -> {
                boolean allowed = service.allowRequest(freeUser);
                System.out.println("Thread " + Thread.currentThread().getId() + " Allowed: " + allowed);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}