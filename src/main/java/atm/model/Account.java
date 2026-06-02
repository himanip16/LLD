package atm.model;

import atm.exception.InsufficientFundsException;

public class Account {
    private final String accountNumber;
    private double balance; // Protected by lock at service layer

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    public void debit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        if (this.balance < amount) throw new InsufficientFundsException();
        this.balance -= amount;
    }

    public void credit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        this.balance += amount;
    }

    public double getBalance() { return balance; }
}