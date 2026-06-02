package atm.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BankService {
    private final Map<String, BigDecimal> accountLedger = new ConcurrentHashMap<>();
    private final Map<String, String> cardToAccountMap = new ConcurrentHashMap<>();
    private final Set<UUID> processedIdempotencyKeys = Collections.synchronizedSet(new HashSet<>());

    public BankService() {
        cardToAccountMap.put("CARD_IN_456", "ACC_PRIMARY_01");
        accountLedger.put("ACC_PRIMARY_01", new BigDecimal("50000.00"));
    }

    public String authenticate(String cardNumber, String pin) {
        if ("4321".equals(pin) && cardToAccountMap.containsKey(cardNumber)) {
            return cardToAccountMap.get(cardNumber);
        }
        return null;
    }

    public BigDecimal fetchAccountBalance(String accountId) {
        return accountLedger.getOrDefault(accountId, BigDecimal.ZERO);
    }

    public void debitAccount(String accountId, BigDecimal amount, UUID idempotencyKey) {
        if (processedIdempotencyKeys.contains(idempotencyKey)) {
            System.out.println("Idempotency token matched. Double debit prevented.");
            return;
        }

        synchronized (accountId.intern()) {
            BigDecimal activeBalance = fetchAccountBalance(accountId);
            if (activeBalance.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient balance on ledger.");
            }
            accountLedger.put(accountId, activeBalance.subtract(amount));
            processedIdempotencyKeys.add(idempotencyKey);
        }
    }
}