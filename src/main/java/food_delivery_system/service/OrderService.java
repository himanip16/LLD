package food_delivery_system.service;

import food_delivery_system.model.Customer;
import food_delivery_system.model.Order;
import food_delivery_system.model.OrderItem;
import food_delivery_system.model.*;
import food_delivery_system.observer.*;
import food_delivery_system.orderstate.OrderContext;
import food_delivery_system.delivery.DeliveryAssignmentStrategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderService {
    private final Map<String, OrderContext> activeOrders = new ConcurrentHashMap<>();
    private final DeliveryPartnerService deliveryPartnerService;
    private final DeliveryAssignmentStrategy assignmentStrategy;

    public OrderService(DeliveryPartnerService partnerService, DeliveryAssignmentStrategy strategy) {
        this.deliveryPartnerService = partnerService;
        this.assignmentStrategy = strategy;
    }

    // Creates the order, binds observers, and saves it
    public OrderContext placeOrder(String orderId, Customer customer, Restaurant restaurant, List<OrderItem> items) {
        Order order = new Order(orderId, customer, restaurant, items);
        OrderContext context = new OrderContext(order);

        // Bind core observers to the context
        context.attach(new CustomerNotificationService());
        context.attach(new DeliveryNotificationService());

        activeOrders.put(orderId, context);
        return context;
    }

    // Progresses the order state safely
    public void progressOrderState(String orderId) {
        OrderContext context = activeOrders.get(orderId);
        if (context == null) throw new IllegalArgumentException("Order not found: " + orderId);

        context.nextState();
    }

    // Assigns an available driver based on the strategy
    public synchronized void assignDeliveryPartner(String orderId) {
        OrderContext context = activeOrders.get(orderId);
        if (context == null) return;

        List<DeliveryPartner> availablePool = deliveryPartnerService.getAvailablePartners();
        DeliveryPartner matchedPartner = assignmentStrategy.assignPartner(context.getOrder(), availablePool);

        if (matchedPartner != null && matchedPartner.checkAndSetAvailability(false)) {
            context.getOrder().setDeliveryPartner(matchedPartner);
            System.out.println("[OrderService] Matched Order " + orderId + " to Driver: " + matchedPartner.getName());

            // Advance state once driver is assigned
            context.nextState();
        } else {
            System.out.println("[OrderService] Retrying: No drivers currently available for order: " + orderId);
        }
    }

    public OrderContext getOrderContext(String orderId) {
        return activeOrders.get(orderId);
    }
}