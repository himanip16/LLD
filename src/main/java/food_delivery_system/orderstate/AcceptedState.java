package food_delivery_system.orderstate;


import food_delivery_system.enums.OrderStatus;

public class AcceptedState implements OrderState {

    @Override
    public void next(OrderContext context) {
        context.setState(new PreparingState());
    }

    @Override
    public void cancel(OrderContext context) {
        // Depending on business logic, restaurants might allow cancellation
        // before the kitchen starts cooking.
        context.setState(new CancelledState());
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.ACCEPTED;
    }
}