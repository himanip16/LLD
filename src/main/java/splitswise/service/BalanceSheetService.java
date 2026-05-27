package splitswise.service;


import splitswise.model.*;

public class BalanceSheetService {
    public void updateBalanceSheet(Group group, Expense expense) {
        User paidBy = expense.getPaidBy();
        double totalAmount = expense.getAmount();

        // 1. Update the overall total paid by the user who paid
        BalanceSheet payerSheet = group.getUserBalanceSheets().get(paidBy);
        payerSheet.addTotalPaid(totalAmount);

        // 2. Map and update expenses vs personal dues across participants
        for (Split split : expense.getSplits()) {
            User participant = split.getUser();
            double share = split.getAmount();

            BalanceSheet participantSheet = group.getUserBalanceSheets().get(participant);
            participantSheet.addTotalExpense(share);

            if (!participant.equals(paidBy)) {
                // Participant owes the Payer money
                payerSheet.updateBalance(participant, share);
                // From the participant's perspective, they owe a negative credit balance to the Payer
                participantSheet.updateBalance(paidBy, -share);
            }
        }
    }
}