package rateLimiter;


public record TokenBucketState(double tokens, long lastRefillNano) {}
