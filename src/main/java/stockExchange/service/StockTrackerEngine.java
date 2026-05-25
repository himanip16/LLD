package stockExchange.service;

import stockExchange.observer.PriceFluctuationObserver;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class StockTrackerEngine {
    private final long windowDurationMs;
    private final double fluctuationThresholdPercent;

    private final Map<String, StockWindowStatistics> trackerRegistry = new ConcurrentHashMap<>();
    private final Map<String, List<PriceFluctuationObserver>> alertRegistry = new ConcurrentHashMap<>();
    private final Map<String, Double> lastRecordedPrices = new ConcurrentHashMap<>();

    public StockTrackerEngine(long windowDurationMs, double fluctuationThresholdPercent) {
        this.windowDurationMs = windowDurationMs;
        this.fluctuationThresholdPercent = fluctuationThresholdPercent;
    }

    public void subscribeToAlerts(String symbol, PriceFluctuationObserver observer) {
        alertRegistry.computeIfAbsent(symbol.toUpperCase(),
                k -> new CopyOnWriteArrayList<>()).add(observer);
    }

    /**
     * Central ingestion method optimized for heavy write streaming.
     */
    public void updatePrice(String stockSymbol, double price, long timestamp) {
        String symbolKey = stockSymbol.toUpperCase();

        // Retrieve or atomically instantiate standard metric window tracker block
        StockWindowStatistics stats = trackerRegistry.computeIfAbsent(symbolKey,
                k -> new StockWindowStatistics(windowDurationMs));

        stats.recordPrice(price, timestamp);

        // Handle Alert Telemetry checks
        Double previousPrice = lastRecordedPrices.put(symbolKey, price);
        if (previousPrice != null) {
            double percentChange = Math.abs((price - previousPrice) / previousPrice) * 100.0;
            if (percentChange >= fluctuationThresholdPercent) {
                dispatchAlert(symbolKey, previousPrice, price, percentChange);
            }
        }
    }

    private void dispatchAlert(String symbol, double oldPrice, double newPrice, double change) {
        List<PriceFluctuationObserver> observers = alertRegistry.get(symbol);
        if (observers != null) {
            for (PriceFluctuationObserver obs : observers) {
                // Offloading to an executor thread pool here would be ideal in production
                obs.onMajorFluctuation(symbol, oldPrice, newPrice, change);
            }
        }
    }

    public double getMaxPrice(String stockSymbol, long currentTimestamp) {
        StockWindowStatistics stats = trackerRegistry.get(stockSymbol.toUpperCase());
        return stats != null ? stats.getMax(currentTimestamp) : 0.0;
    }

    public double getMinPrice(String stockSymbol, long currentTimestamp) {
        StockWindowStatistics stats = trackerRegistry.get(stockSymbol.toUpperCase());
        return stats != null ? stats.getMin(currentTimestamp) : 0.0;
    }

    public double getAveragePrice(String stockSymbol, long currentTimestamp) {
        StockWindowStatistics stats = trackerRegistry.get(stockSymbol.toUpperCase());
        return stats != null ? stats.getAverage(currentTimestamp) : 0.0;
    }
}