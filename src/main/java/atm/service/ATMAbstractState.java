package atm.service;

import atm.model.TransactionType;

import javax.smartcardio.Card;

public abstract class ATMAbstractState {
    public void insertCard(ATM atm, Card card) { System.out.println("Oops! Invalid action."); }
    public void authenticatePin(ATM atm, int pin) { System.out.println("Oops! Invalid action."); }
    public void selectTransaction(ATM atm, TransactionType type) { System.out.println("Oops! Invalid action."); }
    public void withdrawCash(ATM atm, int amount) { System.out.println("Oops! Invalid action."); }
    public void ejectCard(ATM atm) { System.out.println("Oops! Invalid action."); }
}
