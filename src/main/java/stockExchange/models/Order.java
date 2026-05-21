package stockExchange.models;

public record Order(
        String orderId,
        String stockSymbol,
        OrderType orderType,
        int quantity,
        double price,
        String traderId
) {}