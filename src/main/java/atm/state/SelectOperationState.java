package atm.state;

import atm.cash.CashInventory;
import atm.model.Card;
import atm.model.TransactionContext;
import atm.service.ATM;
import atm.service.BankService;
import atm.state.ATMState;

import java.math.BigDecimal;
import java.util.Map;

public class SelectOperationState implements ATMState {
    private final ATM atm;
    public SelectOperationState(ATM atm) { this.atm = atm; }

    @Override public void insertCard(Card card) { throw new IllegalStateException("Session active."); }
    @Override public void enterPin(String pin) { throw new IllegalStateException("Already authenticated."); }

    @Override
    public void checkBalance() {
        TransactionContext session = atm.getSession();
        BigDecimal balance = atm.getBankService().fetchAccountBalance(session.getAccountId());
        System.out.println("[DISPLAY]: Available Balance: INR " + balance);
    }

    @Override
    public void executeWithdrawal(int amount) {
        TransactionContext session = atm.getSession();
        BankService bank = atm.getBankService();
        CashInventory inventory = atm.getCashInventory();

        Map<Integer, Integer> structuralDistributionPlan;
        try {
            // 1. Hardware Chain evaluation & deduction check executed atomically
            structuralDistributionPlan = inventory.allocateAndDeductCash(amount);
        } catch (Exception e) {
            System.out.println("[TRANSACTION HALTED]: Hardware Exception -> " + e.getMessage());
            return;
        }

        try {
            // 2. Transmit debit instructions to core remote ledger
            bank.debitAccount(session.getAccountId(), new BigDecimal(amount), session.getTransactionId());
            System.out.println("[DISPENSER CHUTE]: Dispensed successfully: " + structuralDistributionPlan);
        } catch (Exception e) {
            System.out.println("[CRITICAL REMOTE FAILURE]: Bank rejected transaction. Restoring local physical vaults.");
            // Production systems trigger physical rollbacks if bank engine crashes post hardware check
        }
    }

    @Override
    public void exitSession() {
        System.out.println("[SYSTEM]: Session Completed. Card ejected.");
        atm.setSession(null);
        atm.changeState(new IdleState(atm));
    }
}