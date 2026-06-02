package lockerManagement.notification;

public class SmsNotificationService implements NotificationService {
    @Override
    public void sendPickupCode(String packageId, String secureCode, String tokenId) {
        System.out.println("[SMS Sent]: Package " + packageId + " ready. Use code " + secureCode + " or Token ID: " + tokenId);
    }

    @Override
    public void sendExpiryAlert(String packageId, String lockerId) {
        System.out.println("[SMS Sent]: Package " + packageId + " expired and removed from Locker " + lockerId);
    }
}
