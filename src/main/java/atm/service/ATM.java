package atm.service;

import atm.cash.CashInventory;

import atm.model.Card;
import atm.model.TransactionContext;
import atm.state.ATMState;
import atm.state.IdleState;

public class ATM {
    private ATMState currentState;
    private final CashInventory cashInventory;
    private final BankService bankService;
    private TransactionContext activeSession;

    public ATM(BankService bankService) {
        this.bankService = bankService;
        this.cashInventory = new CashInventory();
        this.currentState = new IdleState(this); // Initial state
    }

    public void changeState(ATMState nextState) { this.currentState = nextState; }
    public void setSession(TransactionContext context) { this.activeSession = context; }
    public TransactionContext getSession() { return activeSession; }
    public CashInventory getCashInventory() { return cashInventory; }
    public BankService getBankService() { return bankService; }

    // State Delegation
    public void insertCard(Card card) { currentState.insertCard(card); }
    public void enterPin(String pin) { currentState.enterPin(pin); }
    public void executeWithdrawal(int amount) { currentState.executeWithdrawal(amount); }
    public void checkBalance() { currentState.checkBalance(); }
    public void exitSession() { currentState.exitSession(); }
}