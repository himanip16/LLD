package lockerManagement.state;

import lockerManagement.model.LockerSlot;

public class ReservedState extends BaseState {

    @Override
    public void deposit(LockerSlot slot) {
        // delivery driver physically placed package inside
        slot.state = new OccupiedState();
    }

    @Override
    public void expire(LockerSlot slot) {
        // driver never came within reservation window
        slot.notificationService.notifyReservationExpired(slot.currentPackage);
        slot.clearSlot();
        slot.state = new AvailableState();
    }
}