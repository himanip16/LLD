package lockerManagement.exception;

public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String operation, String stateName) {
        super("Cannot do '" + operation + "' when slot is in state: " + stateName);
    }
}
