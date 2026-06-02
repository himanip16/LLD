package lockerManagement.notification;

import lockerManagement.model.Package;
public interface NotificationService {
    void notifyReservationExpired(Package pkg);
    void notifyPickupSuccess(Package pkg);
    void notifyExpiry(Package pkg);
    void notifyTooManyWrongPins(Package pkg);
}
