package rateLimiter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Edge burst limitation: client can fire 2x maxRequests across window boundary.
 * Known tradeoff of fixed window design.
 */
public class FixedWindowRateLimiter extends RateLimiter {

    private record WindowState(int count, long windowStartNano) {}

    private final ConcurrentHashMap<String, WindowState> windowMap = new ConcurrentHashMap<>();

    public FixedWindowRateLimiter(RateLimitConfig config) {
        super(config);
    }

    @Override
    public boolean allowRequest(String userId) {
        long now = System.nanoTime();
        long windowNanos = config.getWindowInSeconds() * 1_000_000_000L;
        long currentWindowStart = (now / windowNanos) * windowNanos;

        boolean[] allowed = {false};

        windowMap.compute(userId, (key, state) -> {
            if (state == null || state.windowStartNano() != currentWindowStart) {
                allowed[0] = true;
                return new WindowState(1, currentWindowStart);
            }

            if (state.count() < config.getMaxRequests()) {
                allowed[0] = true;
                return new WindowState(state.count() + 1, currentWindowStart);
            }

            return state;
        });

        return allowed[0];
    }
}