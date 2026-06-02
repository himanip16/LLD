package lockerManagement.model;

import lockerManagement.notification.NotificationService;
import lockerManagement.service.ReturnService;
import lockerManagement.state.AvailableState;
import lockerManagement.state.LockerSlotState;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;
import lockerManagement.model.Package;

public class LockerSlot {
    public final String slotId;
    public final Size size;
    public LockerSlotState state;

    public Package currentPackage;
    public String pin;
    public LocalDateTime reservedAt;
    public int failedPinAttempts = 0;

    public final NotificationService notificationService;
    public final ReturnService returnService;

    // one lock per slot — only one thread can operate on a slot at a time
    public final ReentrantLock lock = new ReentrantLock();

    public LockerSlot(String slotId, Size size,
                      NotificationService notificationService,
                      ReturnService returnService) {
        this.slotId = slotId;
        this.size = size;
        this.state = new AvailableState();
        this.notificationService = notificationService;
        this.returnService = returnService;
    }

    public void reserve(Package pkg, String pin) {
        state.reserve(this, pkg, pin);
    }

    public void deposit() {
        state.deposit(this);
    }

    public void pickup(String enteredPin) {
        state.pickup(this, enteredPin);
    }

    public void expire() {
        state.expire(this);
    }

    public void markOutOfOrder() {
        state.markOutOfOrder(this);
    }

    public void markAvailable() {
        state.markAvailable(this);
    }

    public boolean isAvailable() {
        return state instanceof AvailableState;
    }

    public void clearSlot() {
        this.currentPackage = null;
        this.pin = null;
        this.reservedAt = null;
    }
}