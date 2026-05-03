package food_delivery_system.service;

import food_delivery_system.model.DeliveryPartner;
import food_delivery_system.model.Location;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeliveryPartnerService {
    private final Map<String, DeliveryPartner> driverRegistry = new ConcurrentHashMap<>();

    public void registerPartner(DeliveryPartner partner) {
        driverRegistry.put(partner.getId(), partner);
    }

    public List<DeliveryPartner> getAvailablePartners() {
        return driverRegistry.values().stream()
                .filter(DeliveryPartner::isAvailable)
                .toList();
    }

    public void updateDriverLocation(String driverId, Location newLocation) {
        DeliveryPartner partner = driverRegistry.get(driverId);
        if (partner != null) {
            // Update location internally in domain object (omitted for brevity)
        }
    }
}