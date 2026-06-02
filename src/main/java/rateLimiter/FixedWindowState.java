package rateLimiter;

public record FixedWindowState(int count, long windowStartNano) {}
