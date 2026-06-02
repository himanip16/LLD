package atm.state;

import atm.model.Card;

public interface ATMState {
    void insertCard(Card card);
    void enterPin(String pin);
    void executeWithdrawal(int amount);
    void checkBalance();
    void exitSession();
}