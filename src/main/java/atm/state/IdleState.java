package atm.state;

import atm.model.TransactionContext;
import atm.service.ATM;
import atm.state.ATMState;
import atm.state.HasCardState;

import atm.model.Card;


public class IdleState implements ATMState {
    private final ATM atm;
    public IdleState(ATM atm) { this.atm = atm; }

    @Override
    public void insertCard(Card card) {
        System.out.println("[SYSTEM]: Card read successfully.");
        atm.setSession(new TransactionContext(card, null));
        atm.changeState(new HasCardState(atm));
    }



    @Override public void enterPin(String pin) { throw new IllegalStateException("Insert card first."); }
    @Override public void executeWithdrawal(int amount) { throw new IllegalStateException("Insert card first."); }
    @Override public void checkBalance() { throw new IllegalStateException("Insert card first."); }
    @Override public void exitSession() { throw new IllegalStateException("No active card session."); }
}