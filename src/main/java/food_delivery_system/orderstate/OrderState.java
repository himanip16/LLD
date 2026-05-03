package food_delivery_system.orderstate;

import food_delivery_system.enums.OrderStatus;

public interface OrderState {
    void next(OrderContext context);

    void cancel(OrderContext context);

    OrderStatus getStatus();
}
