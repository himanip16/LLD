package food_delivery_system.orderstate;

import food_delivery_system.enums.OrderStatus;

public class ReadyForPickupState implements OrderState {

    @Override
    public void next(OrderContext context) {
        // The delivery partner picks up the order and departs
        context.setState(new OutForDeliveryState());
    }

    @Override
    public void cancel(OrderContext context) {
        throw new IllegalStateException("Cannot cancel an order that is ready for pickup.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.READY_FOR_PICKUP;
    }
}