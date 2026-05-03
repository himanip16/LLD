package food_delivery_system;

import food_delivery_system.delivery.DeliveryAssignmentStrategy;
import food_delivery_system.delivery.NearestNeighborAssignmentStrategy;
import food_delivery_system.model.*;
import food_delivery_system.orderstate.OrderContext;
import food_delivery_system.service.*;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       INITIALIZING FOOD DELIVERY SYSTEM        ");
        System.out.println("=================================================\n");

        // 1. Initialize Services
        RestaurantService restaurantService = new RestaurantService();
        DeliveryPartnerService deliveryPartnerService = new DeliveryPartnerService();

        // Inject dependencies into the OrderService
        DeliveryAssignmentStrategy assignmentStrategy = new NearestNeighborAssignmentStrategy();
        OrderService orderService = new OrderService(deliveryPartnerService, assignmentStrategy);

        // 2. Setup Data: Restaurant & Menus
        Location restLocation = new Location(12.9800, 77.5900);
        MenuItem pizza = new MenuItem("M1", "Sourdough Margherita Pizza", 550.0);
        MenuItem tiramisu = new MenuItem("M2", "Classic Tiramisu", 320.0);
        Menu menu = new Menu(Arrays.asList(pizza, tiramisu));

        Restaurant restaurant = new Restaurant("R01", "Trattoria Bella", restLocation, menu);
        restaurantService.registerRestaurant(restaurant);
        System.out.println("[Init] Registered Restaurant: " + restaurant.getName());

        // 3. Setup Data: Delivery Partners
        Location driverLoc1 = new Location(12.9815, 77.5910); // Very close to the restaurant
        Location driverLoc2 = new Location(13.0100, 77.6200); // Further away

        DeliveryPartner partner1 = new DeliveryPartner("D01", "Rohan", driverLoc1);
        DeliveryPartner partner2 = new DeliveryPartner("D02", "Vikram", driverLoc2);

        deliveryPartnerService.registerPartner(partner1);
        deliveryPartnerService.registerPartner(partner2);
        System.out.println("[Init] Registered Delivery Partners: Rohan, Vikram\n");

        // 4. Setup Data: Customer
        Location customerLocation = new Location(12.9716, 77.5946);
        Customer customer = new Customer("C01", "Anya", customerLocation);

        // 5. Order Placement
        System.out.println("=== 1. CUSTOMER PLACES ORDER ===");
        List<OrderItem> cartItems = Arrays.asList(
                new OrderItem(pizza, 2),
                new OrderItem(tiramisu, 1)
        );

        OrderContext orderContext = orderService.placeOrder("ORD-101", customer, restaurant, cartItems);
        System.out.println("Order status: " + orderContext.getOrder().getStatus());

        // 6. Restaurant Accepts Order
        System.out.println("\n=== 2. RESTAURANT ACCEPTS ORDER ===");
        orderService.progressOrderState("ORD-101");

        // 7. Restaurant Starts Preparation
        System.out.println("\n=== 3. KITCHEN PREPARING ===");
        orderService.progressOrderState("ORD-101");

        // 8. Assign Delivery Partner via Strategy Pattern
        System.out.println("\n=== 4. SYSTEM ASSIGNING DELIVERY PARTNER ===");
        orderService.assignDeliveryPartner("ORD-101");

        // At this point, Rohan (closest) should be assigned, and Order goes to READY_FOR_PICKUP
        System.out.println("Assigned Partner: " + orderContext.getOrder().getDeliveryPartner().getName());

        // 9. Delivery Partner picks up the food
        System.out.println("\n=== 5. DRIVER OUT FOR DELIVERY ===");
        orderService.progressOrderState("ORD-101");

        // 10. Delivery Partner delivers the food
        System.out.println("\n=== 6. ORDER DELIVERED ===");
        orderService.progressOrderState("ORD-101");

        System.out.println("\n=================================================");
        System.out.println("       LIFECYCLE COMPLETE FOR ORDER ORD-101     ");
        System.out.println("=================================================");
    }
}