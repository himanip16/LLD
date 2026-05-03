package food_delivery_system.delivery;

import food_delivery_system.model.DeliveryPartner;
import food_delivery_system.model.Location;
import food_delivery_system.model.Order;

import java.util.List;

public class NearestNeighborAssignmentStrategy implements DeliveryAssignmentStrategy {
    @Override
    public DeliveryPartner assignPartner(Order order, List<DeliveryPartner> availablePartners) {
        Location restaurantLoc = order.getRestaurant().getLocation();
        DeliveryPartner bestMatch = null;
        double minDistance = Double.MAX_VALUE;

        for (DeliveryPartner partner : availablePartners) {
            double distance = calculateDistance(partner.getLocation(), restaurantLoc);
            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = partner;
            }
        }
        return bestMatch;
    }

    private double calculateDistance(Location l1, Location l2) {
        // Implementation of basic Euclidean or Haversine distance
        return Math.sqrt(Math.pow(l1.getLatitude() - l2.getLatitude(), 2) +
                Math.pow(l1.getLongitude() - l2.getLongitude(), 2));
    }
}