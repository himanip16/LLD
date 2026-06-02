package atm.model;

import java.util.UUID;

public class TransactionContext {
    private final Card card;
    private final String accountId;
    private final UUID transactionId;

    public TransactionContext(Card card, String accountId) {
        this.card = card;
        this.accountId = accountId;
        this.transactionId = UUID.randomUUID(); // Idempotency token
    }
    public Card getCard() { return card; }
    public String getAccountId() { return accountId; }
    public UUID getTransactionId() { return transactionId; }
}