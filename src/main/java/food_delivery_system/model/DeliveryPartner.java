package food_delivery_system.model;

import lombok.Getter;

@Getter
public class DeliveryPartner extends User {
    private boolean isAvailable;

    public DeliveryPartner(String id, String name, Location location) {
        super(id, name, location);
        this.isAvailable = true;
    }

    public synchronized boolean checkAndSetAvailability(boolean available) {
        if (this.isAvailable == available) return false;
        this.isAvailable = available;
        return true;
    }
}
