package stockExchange.service;

import stockExchange.models.*;
import stockExchange.models.Trader;
import stockExchange.observer.ExchangeObserver;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class StockExchange {
    // Maps Ticker Symbol -> List of subscribed observers
    private final Map<String, List<ExchangeObserver>> subscriptionRegistry = new ConcurrentHashMap<>();

    // Tracks active traders registered on the engine
    private final Map<String, Trader> registeredTraders = new ConcurrentHashMap<>();

    public void registerTrader(Trader trader) {
        registeredTraders.put(trader.traderId(), trader);
    }

    // --- Institutional Subscription Capability Management ---
    public void subscribe(String traderId, String stockSymbol, ExchangeObserver observer) {
        Trader trader = registeredTraders.get(traderId);
        if (trader == null) {
            throw new IllegalArgumentException("Trader not registered.");
        }

        // Guard Rule: Enforce Role Capability Boundaries
        if (trader.type() != TraderType.INSTITUTIONAL) {
            throw new SecurityException("Access Denied: Individual traders cannot subscribe to order books.");
        }

        // Compute if absent gives us atomic, thread-safe collection initialization
        subscriptionRegistry.computeIfAbsent(stockSymbol.toUpperCase(),
                k -> new CopyOnWriteArrayList<>()).add(observer);

        System.out.println("✅ Subscribed Institutional Trader [" + trader.name() + "] to Ticker: " + stockSymbol);
    }

    public void unsubscribe(String traderId, String stockSymbol) {
        List<ExchangeObserver> observers = subscriptionRegistry.get(stockSymbol.toUpperCase());
        if (observers != null) {
            observers.removeIf(obs -> obs.getObserverId().equals(traderId));
            System.out.println("❌ Unsubscribed Trader ID [" + traderId + "] from Ticker: " + stockSymbol);
        }
    }

    // --- Core Order Intake Processing ---
    public void placeOrder(String stockSymbol, OrderType type, int quantity, double price, String traderId) {
        Trader trader = registeredTraders.get(traderId);
        if (trader == null) throw new IllegalArgumentException("Invalid execution context: Trader missing.");

        // Construct immutable runtime Order payload instance
        Order newOrder = new Order(UUID.randomUUID().toString(), stockSymbol.toUpperCase(), type, quantity, price, traderId);

        System.out.printf("%n📥 Order Received -> Trader: %s | Ticker: %s | %s %d shares @ $%.2f%n",
                trader.name(), newOrder.stockSymbol(), newOrder.orderType(), newOrder.quantity(), newOrder.price());

        // Process internal matching engine rules here (omitted for scope)

        // Fan out the event to observers immediately
        broadcastToObservers(newOrder);
    }

    private void broadcastToObservers(Order order) {
        List<ExchangeObserver> observers = subscriptionRegistry.get(order.stockSymbol());
        if (observers != null && !observers.isEmpty()) {
            // Iterating over a CopyOnWriteArrayList snapshot ensures lock-free concurrent reads
            for (ExchangeObserver observer : observers) {
                observer.onOrderPlaced(order);
            }
        }
    }
}