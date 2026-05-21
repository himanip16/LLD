package stockExchange.observer;

import stockExchange.models.Order;
import stockExchange.models.Trader;

public class InstitutionalNotificationTerminal implements ExchangeObserver {
    private final Trader institutionalTrader;

    public InstitutionalNotificationTerminal(Trader trader) {
        this.institutionalTrader = trader;
    }

    @Override
    public void onOrderPlaced(Order order) {
        // Immediate realtime terminal broadcast execution simulation
        System.out.printf("🚨 [TERMINAL NOTIFICATION] Trader %s received update -> " +
                        "Order Placed on Ticker %s: %s %d units @ $%.2f%n",
                institutionalTrader.name(), order.stockSymbol(), order.orderType(),
                order.quantity(), order.price());
    }

    @Override
    public String getObserverId() {
        return institutionalTrader.traderId();
    }
}