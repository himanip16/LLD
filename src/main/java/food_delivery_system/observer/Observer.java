package food_delivery_system.observer;

import food_delivery_system.model.Order;

public interface Observer {
    void update(Order order);
}
