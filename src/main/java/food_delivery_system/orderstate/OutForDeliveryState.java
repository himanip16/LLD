package food_delivery_system.orderstate;

import food_delivery_system.enums.OrderStatus;

public class OutForDeliveryState implements OrderState {

    @Override
    public void next(OrderContext context) {
        // The delivery partner reaches the customer and hands over the food
        context.setState(new DeliveredState());
    }

    @Override
    public void cancel(OrderContext context) {
        throw new IllegalStateException("Cannot cancel an order that is already out for delivery.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.OUT_FOR_DELIVERY;
    }
}