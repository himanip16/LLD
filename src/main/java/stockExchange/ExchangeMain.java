package stockExchange;

import stockExchange.models.OrderType;
import stockExchange.models.Trader;
import stockExchange.models.TraderType;
import stockExchange.observer.InstitutionalNotificationTerminal;
import stockExchange.service.StockExchange;

public class ExchangeMain {
    public static void main(String[] args) {
        StockExchange primaryExchange = new StockExchange();

        // 1. Provision and register users across structural types
        Trader t1 = new Trader("TRADER_001", "Alpha Capital (T1)", TraderType.INSTITUTIONAL);
        Trader t2 = new Trader("TRADER_002", "Himani Individual (T2)", TraderType.INDIVIDUAL);
        Trader t3 = new Trader("TRADER_003", "Retail Trader (T3)", TraderType.INDIVIDUAL);

        primaryExchange.registerTrader(t1);
        primaryExchange.registerTrader(t2);
        primaryExchange.registerTrader(t3);

        // 2. Setup subscription hooks for the Institutional Trader
        InstitutionalNotificationTerminal t1Terminal = new InstitutionalNotificationTerminal(t1);
        primaryExchange.subscribe(t1.traderId(), "ABC", t1Terminal);

        // 3. System Invariant Guard Test: Attempt to hook an individual trader to the observer logs
        try {
            System.out.println("\n--- Testing Security Policy Boundary Rule ---");
            primaryExchange.subscribe(t2.traderId(), "ABC", new InstitutionalNotificationTerminal(t2));
        } catch (SecurityException e) {
            System.out.println("Blocked Unauthorized Subscription: " + e.getMessage());
        }

        // 4. Execution Flow A: Individual Trader T2 places an order for ABC
        // Result Expectation: Institutional Terminal T1 receives an immediate callback event.
        primaryExchange.placeOrder("ABC", OrderType.BUY, 500, 152.50, t2.traderId());

        // 5. Execution Flow B: Individual Trader T3 places an order for XYZ
        // Result Expectation: T1 does NOT receive notifications because they never registered for XYZ.
        primaryExchange.placeOrder("XYZ", OrderType.SELL, 1200, 44.10, t3.traderId());
    }
}