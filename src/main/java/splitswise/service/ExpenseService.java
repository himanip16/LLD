package splitswise.service;


import splitswise.model.*;
import splitswise.strategy.SplitStrategy;
import splitswise.strategy.SplitStrategyFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExpenseService {
    private BalanceSheetService balanceSheetService;

    public ExpenseService(BalanceSheetService balanceSheetService) {
        this.balanceSheetService = balanceSheetService;
    }

    public void addExpense(Group group, String description, double amount, User paidBy, List<User> participants, SplitType splitType, Map<User, Double> metadata) {
        SplitStrategy strategy = SplitStrategyFactory.getStrategy(splitType);
        List<Split> splits = strategy.split(amount, participants, metadata);

        Expense expense = new Expense(UUID.randomUUID().toString(), description, amount, paidBy, splits, splitType);
        group.addExpense(expense);

        balanceSheetService.updateBalanceSheet(group, expense);
    }
}