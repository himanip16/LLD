package stockExchange.observer;

public interface PriceFluctuationObserver {
    void onMajorFluctuation(String symbol, double oldPrice, double newPrice, double percentageChange);
    String getObserverId();
}