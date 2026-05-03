package food_delivery_system.orderstate;

import food_delivery_system.enums.OrderStatus;

public class CancelledState implements OrderState {

    @Override
    public void next(OrderContext context) {
        throw new IllegalStateException("Cannot proceed from a cancelled order.");
    }

    @Override
    public void cancel(OrderContext context) {
        throw new IllegalStateException("Order is already cancelled.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.CANCELLED;
    }
}