package food_delivery_system.orderstate;

import food_delivery_system.model.Order;
import food_delivery_system.observer.Observer;
import food_delivery_system.observer.OrderSubject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class OrderContext implements OrderSubject {
    private OrderState state;
    private final Order order;
    private final List<Observer> observers = new ArrayList<>();

    public OrderContext(Order order) {
        this.order = order;
        this.state = new PlacedState();
    }

    public void setState(OrderState state) {
        this.state = state;
        this.order.setStatus(state.getStatus());
        notifyObservers();
    }

    public void nextState() { state.next(this); }
    public void cancelOrder() { state.cancel(this); }

    @Override
    public void attach(Observer observer) {

    }

    @Override
    public void detach(Observer observer) {

    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) { observer.update(this.order); }
    }
}