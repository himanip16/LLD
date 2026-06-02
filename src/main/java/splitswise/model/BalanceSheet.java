package splitswise.model;

// BalanceSheet.java

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.DoubleAccumulator;

public class BalanceSheet {
    // Thread-safe accumulation variants for numeric primitives
    private final DoubleAccumulator totalPaid = new DoubleAccumulator(Double::sum, 0.0);
    private final DoubleAccumulator totalExpense = new DoubleAccumulator(Double::sum, 0.0);

    // Concurrent map tracking ledger entries against peer users
    private final ConcurrentHashMap<User, Double> userBalances = new ConcurrentHashMap<>();

    public double getTotalPaid() { return totalPaid.get(); }
    public double getTotalExpense() { return totalExpense.get(); }
    public Map<User, Double> getUserBalances() { return userBalances; }

    public void addTotalPaid(double amount) { this.totalPaid.accumulate(amount); }
    public void addTotalExpense(double amount) { this.totalExpense.accumulate(amount); }

    public void updateBalance(User user, double amount) {
        // Atomic transaction mapping via concurrent compute blocks
        userBalances.compute(user, (key, currentBalance) -> {
            double nextBalance = (currentBalance == null) ? amount : currentBalance + amount;
            return (Math.abs(nextBalance) < 0.001) ? null : nextBalance;
        });
    }

    public void clearBalances() {
        this.userBalances.clear();
    }
}