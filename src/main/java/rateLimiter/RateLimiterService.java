package rateLimiter;

import java.util.concurrent.ConcurrentHashMap;

public class RateLimiterService {
    private final ConcurrentHashMap<UserTier, RateLimiter> tierLimiters = new ConcurrentHashMap<>();

    public RateLimiterService() {
        // Uniform architectural algorithm approach, decoupled purely via scale settings
        tierLimiters.put(UserTier.FREE, new TokenBucketRateLimiter(new RateLimitConfig(10, 60)));
        tierLimiters.put(UserTier.PREMIUM, new TokenBucketRateLimiter(new RateLimitConfig(100, 60)));
    }

    public boolean allowRequest(User user) {
        RateLimiter limiter = tierLimiters.get(user.tier());
        return limiter != null && limiter.allowRequest(user.userId());
    }
}