package lockerManagement.state;

import lockerManagement.model.LockerSlot;

public class OutOfOrderState extends BaseState {

    @Override
    public void markAvailable(LockerSlot slot) {
        // maintenance fixed the locker
        slot.failedPinAttempts = 0;
        slot.state = new AvailableState();
    }

    @Override
    public void markOutOfOrder(LockerSlot slot) {
        // already out of order — do nothing, no error
    }
}
