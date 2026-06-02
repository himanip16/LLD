package rateLimiter;

public abstract class RateLimiter {
    protected final RateLimitConfig config;

    public RateLimiter(RateLimitConfig config) {
        this.config = config;
    }

    public abstract boolean allowRequest(String userId);
}