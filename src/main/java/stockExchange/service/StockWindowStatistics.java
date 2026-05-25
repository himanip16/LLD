package stockExchange.service;


import stockExchange.models.PriceTick;
import java.util.ArrayDeque;
import java.util.Deque;

public class StockWindowStatistics {
    private final long windowDurationMs;

    private final Deque<PriceTick> tickQueue = new ArrayDeque<>();
    private final Deque<PriceTick> maxDeque = new ArrayDeque<>();
    private final Deque<PriceTick> minDeque = new ArrayDeque<>();

    private double runningSum = 0.0;

    public StockWindowStatistics(long windowDurationMs) {
        this.windowDurationMs = windowDurationMs;
    }

    /**
     * Records a new price point and purges expired ticks synchronously.
     * Amortized Time Complexity: O(1)
     */
    public synchronized void recordPrice(double price, long timestamp) {
        PriceTick newTick = new PriceTick(price, timestamp);

        // 1. Ingest into standard queue and add to average tracking
        tickQueue.addLast(newTick);
        runningSum += price;

        // 2. Maintain Monotonic Deque for Max (keep it descending)
        while (!maxDeque.isEmpty() && maxDeque.peekLast().price() <= price) {
            maxDeque.removeLast();
        }
        maxDeque.addLast(newTick);

        // 3. Maintain Monotonic Deque for Min (keep it ascending)
        while (!minDeque.isEmpty() && minDeque.peekLast().price() >= price) {
            minDeque.removeLast();
        }
        minDeque.addLast(newTick);

        // 4. Clean out old data points
        evictExpiredTicks(timestamp);
    }

    /**
     * Drops data points that fall out of the trailing time window horizon.
     */
    public synchronized void evictExpiredTicks(long currentTimestamp) {
        long boundaryTime = currentTimestamp - windowDurationMs;

        while (!tickQueue.isEmpty() && tickQueue.peekFirst().timestamp() < boundaryTime) {
            PriceTick expired = tickQueue.removeFirst();
            runningSum -= expired.price();

            // Evict from min/max structures if they matches the identity reference
            if (!maxDeque.isEmpty() && maxDeque.peekFirst() == expired) {
                maxDeque.removeFirst();
            }
            if (!minDeque.isEmpty() && minDeque.peekFirst() == expired) {
                minDeque.removeFirst();
            }
        }
    }

    public synchronized double getMax(long currentTimestamp) {
        evictExpiredTicks(currentTimestamp);
        return maxDeque.isEmpty() ? 0.0 : maxDeque.peekFirst().price();
    }

    public synchronized double getMin(long currentTimestamp) {
        evictExpiredTicks(currentTimestamp);
        return minDeque.isEmpty() ? 0.0 : minDeque.peekFirst().price();
    }

    public synchronized double getAverage(long currentTimestamp) {
        evictExpiredTicks(currentTimestamp);
        return tickQueue.isEmpty() ? 0.0 : (runningSum / tickQueue.size());
    }
}
