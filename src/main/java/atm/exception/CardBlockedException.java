package atm.exception;

public class CardBlockedException extends ATMException {
    public CardBlockedException() {
        super("Card has been blocked due to excessive PIN failures.");
    }
}
