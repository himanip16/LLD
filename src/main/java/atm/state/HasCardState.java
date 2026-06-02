package atm.state;

import atm.model.Card;
import atm.model.TransactionContext;
import atm.service.ATM;

public class HasCardState implements ATMState {
    private final ATM atm;
    public HasCardState(ATM atm) { this.atm = atm; }

    @Override public void insertCard(Card card) { throw new IllegalStateException("Card already inside."); }



    @Override
    public void enterPin(String pin) {
        Card card = atm.getSession().getCard();
        String verifiedAccountId = atm.getBankService().authenticate(card.getCardNumber(), pin);

        if (verifiedAccountId != null) {
            System.out.println("[SYSTEM]: Identity authenticated successfully.");
            atm.setSession(new TransactionContext(card, verifiedAccountId));
            atm.changeState(new SelectOperationState(atm));
        } else {
            System.out.println("[SECURITY ERROR]: Invalid PIN.");
            exitSession(); // Safety ejection path
        }
    }
    @Override public void executeWithdrawal(int amount) { throw new IllegalStateException("Enter PIN first."); }
    @Override public void checkBalance() { throw new IllegalStateException("Enter PIN first."); }
    @Override
    public void exitSession() {
        System.out.println("[SYSTEM]: Evicting context. Ejecting card.");
        atm.setSession(null);
        atm.changeState(new IdleState(atm));
    }
}