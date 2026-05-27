package splitswise.model;


import java.util.List;

public class Expense {
    private String id;
    private String description;
    private double amount;
    private User paidBy;
    private List<Split> splits;
    private SplitType splitType;

    public Expense(String id, String description, double amount, User paidBy, List<Split> splits, SplitType splitType) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.splitType = splitType;
    }

    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public User getPaidBy() { return paidBy; }
    public List<Split> getSplits() { return splits; }
    public SplitType getSplitType() { return splitType; }
}