package food_delivery_system.delivery;

import food_delivery_system.model.DeliveryPartner;
import food_delivery_system.model.Order;

import java.util.List;

public interface DeliveryAssignmentStrategy {
    DeliveryPartner assignPartner(Order order, List<DeliveryPartner> availablePartners);
}
