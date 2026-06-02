package atm.exception;

public class InsufficientFundsException extends ATMException {
    public InsufficientFundsException() {
        super("Account balance is insufficient.");
    }
}
