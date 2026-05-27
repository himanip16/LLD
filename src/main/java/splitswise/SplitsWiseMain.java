package splitswise;

// Main.java

import splitswise.model.BalanceSheet;
import splitswise.model.Group;
import splitswise.model.SplitType;
import splitswise.model.User;
import splitswise.repository.GroupRepository;
import splitswise.repository.InMemoryGroupRepository;
import splitswise.service.BalanceSheetService;
import splitswise.service.DebtSimplificationService;
import splitswise.service.ExpenseService;
import splitswise.service.GroupService;

import java.util.Arrays;
import java.util.List;

public class SplitsWiseMain {
    public static void main(String[] args) {
        // Init Services
        GroupRepository groupRepo = new InMemoryGroupRepository();
        BalanceSheetService balanceSheetService = new BalanceSheetService();
        ExpenseService expenseService = new ExpenseService(balanceSheetService);
        DebtSimplificationService simplificationService = new DebtSimplificationService();
        GroupService groupService = new GroupService(groupRepo, expenseService, simplificationService);

        // Create Users
        User shubh = new User("1", "Shubh");
        User bob = new User("2", "Bob");
        User tom = new User("3", "Tom");
        List<User> members = Arrays.asList(shubh, bob, tom);

        // Create Group
        String groupId = groupService.createGroup("Goa Trip", members);

        // 1. Day 1 Lunch Expense: Shubh pays $100, split equally between Shubh and Bob
        groupService.addExpense(groupId, "Lunch Day 1", 100.0, shubh, Arrays.asList(shubh, bob), SplitType.EQUAL, null);

        // 2. Day 2 Lunch Expense: Bob pays $100, split equally between Bob and Tom
        groupService.addExpense(groupId, "Lunch Day 2", 100.0, bob, Arrays.asList(bob, tom), SplitType.EQUAL, null);

        System.out.println("--- Balance Sheet BEFORE Simplification ---");
        printBalances(groupService.getGroupDetails(groupId));

        // 3. Trigger simplification algorithm optimization
        groupService.simplifyGroupDebts(groupId);

        System.out.println("\n--- Balance Sheet AFTER Simplification ---");
        printBalances(groupService.getGroupDetails(groupId));
    }

    private static void printBalances(Group group) {
        for (User member : group.getMembers()) {
            System.out.println("\nUser: " + member.getName());
            BalanceSheet sheet = group.getUserBalanceSheets().get(member);
            System.out.println("  Total Paid: $" + sheet.getTotalPaid());
            System.out.println("  Total Share Obligation: $" + sheet.getTotalExpense());
            System.out.println("  Relationships:");
            sheet.getUserBalances().forEach((otherUser, balance) -> {
                if (balance > 0) {
                    System.out.println("    Gets $" + balance + " from " + otherUser.getName());
                } else {
                    System.out.println("    Owes $" + Math.abs(balance) + " to " + otherUser.getName());
                }
            });
        }
    }
}
