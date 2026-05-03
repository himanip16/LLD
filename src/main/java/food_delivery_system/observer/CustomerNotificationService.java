package food_delivery_system.observer;

import food_delivery_system.model.Order;

public class CustomerNotificationService implements Observer {
    @Override
    public void update(Order order) {
        System.out.println("Notification to Customer " + order.getCustomer().getName() +
                ": Your order " + order.getOrderId() + " is now " + order.getStatus());
    }
}
