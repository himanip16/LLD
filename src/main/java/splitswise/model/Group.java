package splitswise.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {
    private String id;
    private String name;
    private List<User> members;
    private List<Expense> expenses;
    private Map<User, BalanceSheet> userBalanceSheets;

    public Group(String id, String name, List<User> members) {
        this.id = id;
        this.name = name;
        this.members = members;
        this.expenses = new ArrayList<>();
        this.userBalanceSheets = new HashMap<>();
        for (User member : members) {
            userBalanceSheets.put(member, new BalanceSheet());
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<User> getMembers() { return members; }
    public List<Expense> getExpenses() { return expenses; }
    public Map<User, BalanceSheet> getUserBalanceSheets() { return userBalanceSheets; }

    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
            userBalanceSheets.put(user, new BalanceSheet());
        }
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
}
