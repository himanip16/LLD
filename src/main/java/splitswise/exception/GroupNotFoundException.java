package splitswise.exception;

public class GroupNotFoundException extends SplitwiseException {
    public GroupNotFoundException(String id) {
        super("Group not found with ID: " + id);
    }
}
