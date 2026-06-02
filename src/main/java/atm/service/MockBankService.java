//package atm.service;
//
//import atm.model.Account;
//
//import java.util.Collections;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class MockBankService implements IBankService {
//    private final Map<String, String> cardToPinHash = new ConcurrentHashMap<>();
//    private final Map<String, Account> cardToAccount = new ConcurrentHashMap<>();
//    private final Set<String> processedTransactions = Collections.newSetFromMap(new ConcurrentHashMap<>());
//
//    public void registerCard(String cardNumber, int pin, Account account) {
//        // Simple mock hash simulation
//        cardToPinHash.put(cardNumber, String.valueOf(Objects.hash(pin)));
//        cardToAccount.put(cardNumber, account);
//    }
//
//    @Override
//    public boolean validatePin(String cardNumber, int pin) {
//        String hash = String.valueOf(Objects.hash(pin));
//        return hash.equals(cardToPinHash.get(cardNumber));
//    }
//
//    @Override
//    public Account getAccount(String cardNumber) {
//        return cardToAccount.get(cardNumber);
//    }
//
//    @Override
//    public synchronized void executeWithdraw(String accountNumber, int amount, String txId) {
//        if (processedTransactions.contains(txId)) {
//            System.out.println("[BANK] Duplicate tx detected. Resending cached confirmation.");
//            return;
//        }
//
//        Account account = cardToAccount.values().stream()
//                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
//                .findFirst()
//                .orElseThrow(() -> new ATMException("Account not found."));
//
//        account.debit(amount);
//        processedTransactions.add(txId);
//    }
//}
