package splitswise.service;

import splitswise.model.BalanceSheet;
import splitswise.model.Group;
import splitswise.model.User;

import java.util.*;

public class DebtSimplificationService {

    private static class Pair {
        User user;
        double balance;
        Pair(User user, double balance) {
            this.user = user;
            this.balance = balance;
        }
    }

    public void simplifyDebts(Group group) {
        Map<User, Double> netBalances = new HashMap<>();

        // Step 1: Calculate net values from the original ledger base metrics
        for (User member : group.getMembers()) {
            BalanceSheet sheet = group.getUserBalanceSheets().get(member);
            double netValue = sheet.getTotalPaid() - sheet.getTotalExpense();
            netBalances.put(member, netValue);
            sheet.clearBalances(); // Wiping out standard granular tracking maps to recompute clean simplified pathways
        }

        // Step 2: Separate into max/min heap buckets using priorities
        PriorityQueue<Pair> maxHeap = new PriorityQueue<>((a, b) -> Double.compare(b.balance, a.balance)); // Receivers
        PriorityQueue<Pair> minHeap = new PriorityQueue<>((a, b) -> Double.compare(a.balance, b.balance)); // Senders

        for (Map.Entry<User, Double> entry : netBalances.entrySet()) {
            if (entry.getValue() > 0.001) {
                maxHeap.add(new Pair(entry.getKey(), entry.getValue()));
            } else if (entry.getValue() < -0.001) {
                minHeap.add(new Pair(entry.getKey(), entry.getValue()));
            }
        }

        // Step 3: Greedy mapping resolution cycle
        while (!maxHeap.isEmpty() && !minHeap.isEmpty()) {
            Pair receiver = maxHeap.poll();
            Pair sender = minHeap.poll();

            double amountToSettle = Math.min(receiver.balance, Math.abs(sender.balance));

            // Record transaction pathways into updated localized user balance sheets
            group.getUserBalanceSheets().get(receiver.user).updateBalance(sender.user, amountToSettle);
            group.getUserBalanceSheets().get(sender.user).updateBalance(receiver.user, -amountToSettle);

            receiver.balance -= amountToSettle;
            sender.balance += amountToSettle;

            if (receiver.balance > 0.001) maxHeap.add(receiver);
            if (sender.balance < -0.001) minHeap.add(sender);
        }
    }
}