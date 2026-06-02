package lockerManagement.notification;

public interface NotificationService {
    void sendPickupCode(String packageId, String secureCode, String tokenId);
    void sendExpiryAlert(String packageId, String lockerId);
}

