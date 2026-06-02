package lockerManagement.exception;

public class InvalidPinException extends RuntimeException {
    public InvalidPinException(String slotId) {
        super("Wrong PIN entered for slot: " + slotId);
    }
}

