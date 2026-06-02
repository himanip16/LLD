package rateLimiter;

import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketRateLimiter extends RateLimiter {

    private final ConcurrentHashMap<String, TokenBucketState> bucketMap = new ConcurrentHashMap<>();

    public TokenBucketRateLimiter(RateLimitConfig config) {
        super(config);
    }

    @Override
    public boolean allowRequest(String userId) {
        long now = System.nanoTime();
        boolean[] allowed = {false};

        bucketMap.compute(userId, (key, state) -> {
            double maxTokens = config.getMaxRequests();
            double refillRate = maxTokens / config.getWindowInSeconds(); // tokens per second

            double currentTokens;
            if (state == null) {
                currentTokens = maxTokens;
            } else {
                double elapsedSeconds = (now - state.lastRefillNano()) / 1_000_000_000.0;
                currentTokens = Math.min(maxTokens, state.tokens() + elapsedSeconds * refillRate);
            }

            if (currentTokens >= 1.0) {
                allowed[0] = true;
                return new TokenBucketState(currentTokens - 1.0, now);
            }

            return new TokenBucketState(currentTokens, state.lastRefillNano());
        });

        return allowed[0];
    }
}