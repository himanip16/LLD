package food_delivery_system.model;

import food_delivery_system.enums.OrderStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class Order {
    private final String orderId;
    private final Customer customer;
    private final Restaurant restaurant;
    private final List<OrderItem> items;
    private OrderStatus status;
    private DeliveryPartner deliveryPartner;
    private final double totalAmount;

    public Order(String orderId, Customer customer, Restaurant restaurant, List<OrderItem> items) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items;
        this.status = OrderStatus.PLACED;
        this.totalAmount = calculateTotal();
    }

    private double calculateTotal() {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    public synchronized void setStatus(OrderStatus status) {
        this.status = status;
    }

    public synchronized void setDeliveryPartner(DeliveryPartner partner) {
        this.deliveryPartner = partner;
    }

    // Getters
}