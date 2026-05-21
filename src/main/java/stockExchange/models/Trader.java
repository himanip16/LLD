package stockExchange.models;

public record Trader(
        String traderId,
        String name,
        TraderType type
) {}