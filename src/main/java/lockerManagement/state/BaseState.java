package lockerManagement.state;

import lockerManagement.exception.InvalidStateTransitionException;
import lockerManagement.model.LockerSlot;
import lockerManagement.model.Package;

public class BaseState implements LockerSlotState {

    public void reserve(LockerSlot slot, Package pkg, String pin) {
        throw new InvalidStateTransitionException("reserve", this.getClass().getSimpleName());
    }

    public void deposit(LockerSlot slot) {
        throw new InvalidStateTransitionException("deposit", this.getClass().getSimpleName());
    }

    public void pickup(LockerSlot slot, String enteredPin) {
        throw new InvalidStateTransitionException("pickup", this.getClass().getSimpleName());
    }

    public void expire(LockerSlot slot) {
        throw new InvalidStateTransitionException("expire", this.getClass().getSimpleName());
    }

    public void markOutOfOrder(LockerSlot slot) {
        // allowed from any state — maintenance can always mark broken
        slot.state = new OutOfOrderState();
    }

    public void markAvailable(LockerSlot slot) {
        throw new InvalidStateTransitionException("markAvailable", this.getClass().getSimpleName());
    }
}
