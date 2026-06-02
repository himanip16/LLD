package splitswise.exception;

public class UserNotInGroupException extends SplitwiseException {
    public UserNotInGroupException(String name) {
        super("User " + name + " is not a registered participant in this group.");
    }
}
