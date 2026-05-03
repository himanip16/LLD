package food_delivery_system.orderstate;

import food_delivery_system.enums.OrderStatus;

public class PlacedState implements OrderState {

    @Override
    public void next(OrderContext context) {
        // From PLACED, the order transitions to ACCEPTED by the restaurant
        context.setState(new AcceptedState());
    }

    @Override
    public void cancel(OrderContext context) {
        // An order can be cancelled immediately after placement
        context.setState(new CancelledState());
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.PLACED;
    }
}