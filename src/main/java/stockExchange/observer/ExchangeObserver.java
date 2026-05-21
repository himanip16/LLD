
package stockExchange.observer;

import stockExchange.models.Order;
import stockExchange.models.Trader;

public interface ExchangeObserver {
    void onOrderPlaced(Order order);
    String getObserverId();
}