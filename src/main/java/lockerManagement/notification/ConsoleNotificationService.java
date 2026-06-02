package lockerManagement.notification;

import lockerManagement.model.Package;

public class ConsoleNotificationService implements NotificationService {

    public void notifyReservationExpired(Package pkg) {
        System.out.println("NOTIFY: Reservation expired for package " + pkg.packageId);
    }

    public void notifyPickupSuccess(Package pkg) {
        System.out.println("NOTIFY: Package " + pkg.packageId + " picked up successfully");
    }

    public void notifyExpiry(Package pkg) {
        System.out.println("NOTIFY: Package " + pkg.packageId + " expired. Initiating return.");
    }

    public void notifyTooManyWrongPins(Package pkg) {
        System.out.println("NOTIFY: Too many wrong PINs for package " + pkg.packageId + ". Slot locked.");
    }
}
