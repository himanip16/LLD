package rateLimiter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RateLimitConfig{
    private int maxRequests;
    private long windowInSeconds;

}
