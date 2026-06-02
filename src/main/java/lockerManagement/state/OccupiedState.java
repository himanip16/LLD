package lockerManagement.state;

import lockerManagement.exception.InvalidPinException;
import lockerManagement.model.LockerSlot;

public class OccupiedState extends BaseState {

    @Override
    public void pickup(LockerSlot slot, String enteredPin) {
        if (!slot.pin.equals(enteredPin)) {
            slot.failedPinAttempts++;

            if (slot.failedPinAttempts >= 3) {
                slot.notificationService.notifyTooManyWrongPins(slot.currentPackage);
                slot.state = new OutOfOrderState();
                throw new InvalidPinException(
                        "Slot " + slot.slotId + " locked after 3 wrong PIN attempts"
                );
            }

            throw new InvalidPinException(
                    "Wrong PIN for slot " + slot.slotId
                            + ". Attempts remaining: " + (3 - slot.failedPinAttempts)
            );
        }

        slot.notificationService.notifyPickupSuccess(slot.currentPackage);
        slot.failedPinAttempts = 0;
        slot.clearSlot();
        slot.state = new AvailableState();
    }

    @Override
    public void expire(LockerSlot slot) {
        // customer did not collect within 3 days
        slot.notificationService.notifyExpiry(slot.currentPackage);
        slot.returnService.initiateReturn(slot.currentPackage);
        slot.clearSlot();
        slot.state = new AvailableState();
    }
}
