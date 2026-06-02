package lockerManagement.state;

import lockerManagement.model.LockerSlot;
import lockerManagement.model.Package;

import java.time.LocalDateTime;

public class AvailableState extends BaseState {

    @Override
    public void reserve(LockerSlot slot, Package pkg, String pin) {
        slot.currentPackage = pkg;
        slot.pin = pin;
        slot.reservedAt = LocalDateTime.now();
        slot.state = new ReservedState();
    }
}
