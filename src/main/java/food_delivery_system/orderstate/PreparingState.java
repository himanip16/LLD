package food_delivery_system.orderstate;

import food_delivery_system.enums.OrderStatus;

public class PreparingState implements OrderState {

    @Override
    public void next(OrderContext context) {
        // Food is ready for pickup by the delivery partner
        context.setState(new ReadyForPickupState());
    }

    @Override
    public void cancel(OrderContext context) {
        // Once preparation starts, cancellations are usually forbidden
        throw new IllegalStateException("Cannot cancel an order that is already being prepared.");
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PREPARING;
    }
}