package lockerManagement.model;

public class DeliveryReceipt {
    public final String slotId;
    public final String pin;

    public DeliveryReceipt(String slotId, String pin) {
        this.slotId = slotId;
        this.pin = pin;
    }
}
