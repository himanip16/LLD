package food_delivery_system.observer;

import food_delivery_system.model.Order;

public class DeliveryNotificationService implements Observer {
    @Override
    public void update(Order order) {
        if (order.getDeliveryPartner() != null) {
            System.out.println("Notification to Partner " + order.getDeliveryPartner().getName() +
                    ": Order " + order.getOrderId() + " is ready for pickup.");
        }
    }
}
