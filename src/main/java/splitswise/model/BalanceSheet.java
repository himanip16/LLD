package splitswise.model;

// BalanceSheet.java

import java.util.HashMap;
import java.util.Map;


public class BalanceSheet {
    private double totalPaid;
    private double totalExpense;
    private Map<User, Double> userBalances;

    public BalanceSheet() {
        this.totalPaid = 0.0;
        this.totalExpense = 0.0;
        this.userBalances = new HashMap<>();
    }

    public double getTotalPaid() { return totalPaid; }
    public double getTotalExpense() { return totalExpense; }
    public Map<User, Double> getUserBalances() { return userBalances; }

    public void addTotalPaid(double amount) { this.totalPaid += amount; }
    public void addTotalExpense(double amount) { this.totalExpense += amount; }

    public void updateBalance(User user, double amount) {
        double currentBalance = userBalances.getOrDefault(user, 0.0) + amount;
        if (Math.abs(currentBalance) < 0.001) {
            userBalances.remove(user);
        } else {
            userBalances.put(user, currentBalance);
        }
    }

    public void clearBalances() {
        this.userBalances.clear();
    }
}
