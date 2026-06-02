package splitswise.service;


import splitswise.exception.InvalidExpenseException;
import splitswise.model.*;
import splitswise.strategy.SplitStrategy;
import splitswise.strategy.SplitStrategyFactory;

import java.util.List;
import java.util.Map;

public class ExpenseService {
    private final BalanceSheetService balanceSheetService;

    public ExpenseService(BalanceSheetService balanceSheetService) {
        this.balanceSheetService = balanceSheetService;
    }

    public void addExpense(Group group, String description, double amount, User paidBy,
                           List<User> participants, SplitType splitType, Map<User, Double> metadata) {

        // --- DEFENSIVE ARGUMENT VALIDATIONS ---
        if (amount <= 0) {
            throw new InvalidExpenseException("Expense collection balances must be strictly positive figures.");
        }
        if (participants == null || participants.isEmpty()) {
            throw new InvalidExpenseException("Every logged expense transaction requires at least one tracking recipient participant.");
        }

        // Assert group membership bounds beforehand
        group.validateMember(paidBy);
        for (User user : participants) {
            group.validateMember(user);
            if (splitType == SplitType.PERCENTAGE && (metadata == null || !metadata.containsKey(user))) {
                throw new InvalidExpenseException("Missing distribution percentage metadata allocation parameter configurations for user: " + user.getName());
            }
        }

        // --- PROCESSING PIPELINE ---
        SplitStrategy strategy = SplitStrategyFactory.getStrategy(splitType);
        var splits = strategy.split(amount, participants, metadata);

       Expense expense = new Expense(
                java.util.UUID.randomUUID().toString(), description, amount, paidBy, splits, splitType
        );

        group.addExpense(expense);
        balanceSheetService.updateBalanceSheet(group, expense);
    }
}