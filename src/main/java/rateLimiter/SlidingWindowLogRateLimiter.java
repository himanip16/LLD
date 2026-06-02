// SlidingWindowLogRateLimiter.java
package rateLimiter;

import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowLogRateLimiter extends RateLimiter {

    private final ConcurrentHashMap<String, ArrayDeque<Long>> userLogMap = new ConcurrentHashMap<>();

    public SlidingWindowLogRateLimiter(RateLimitConfig config) {
        super(config);
    }

    @Override
    public boolean allowRequest(String userId) {
        long now = System.nanoTime();
        long windowNanos = config.getWindowInSeconds() * 1_000_000_000L;
        long windowStart = now - windowNanos;

        boolean[] allowed = {false};

        userLogMap.compute(userId, (key, log) -> {
            if (log == null) log = new ArrayDeque<>();

            while (!log.isEmpty() && log.peekFirst() <= windowStart) {
                log.pollFirst();
            }

            if (log.size() < config.getMaxRequests()) {
                log.addLast(now);
                allowed[0] = true;
            }

            return log;
        });

        return allowed[0];
    }
}