package lockerManagement.state;

import lockerManagement.model.LockerSlot;
import lockerManagement.model.Package;

public interface LockerSlotState {
    void reserve(LockerSlot slot, Package pkg, String pin);
    void deposit(LockerSlot slot);
    void pickup(LockerSlot slot, String enteredPin);
    void expire(LockerSlot slot);
    void markOutOfOrder(LockerSlot slot);
    void markAvailable(LockerSlot slot);
}
