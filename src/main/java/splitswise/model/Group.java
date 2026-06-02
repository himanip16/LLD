package splitswise.model;


import lombok.Getter;
import lombok.Setter;
import splitswise.exception.UserNotInGroupException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
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

    public void addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
            userBalanceSheets.put(user, new BalanceSheet());
        }
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void validateMember(User paidBy) {
        if(!members.contains(paidBy)) {
            throw new UserNotInGroupException(paidBy.getId());
        }
    }
}
