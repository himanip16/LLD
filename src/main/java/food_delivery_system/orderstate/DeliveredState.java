package food_delivery_system.orderstate;

import food_delivery_system.enums.OrderStatus;

public class DeliveredState implements OrderState {

    @Override
    public void next(OrderContext context) {
        throw new IllegalStateException("Order is already delivered. Lifecycle is complete.");
    }

    @Override
    public void cancel(OrderContext context) {
        throw new IllegalStateException("Cannot cancel a completed delivery.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.DELIVERED;
    }
}