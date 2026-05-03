package food_delivery_system.service;

import food_delivery_system.model.Menu;
import food_delivery_system.model.Restaurant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RestaurantService {
    private final Map<String, Restaurant> restaurantRegistry = new ConcurrentHashMap<>();

    public void registerRestaurant(Restaurant restaurant) {
        restaurantRegistry.put(restaurant.getId(), restaurant);
    }

    public Restaurant getRestaurant(String restaurantId) {
        Restaurant restaurant = restaurantRegistry.get(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant not found: " + restaurantId);
        }
        return restaurant;
    }

    public void updateMenu(String restaurantId, Menu newMenu) {
        Restaurant restaurant = getRestaurant(restaurantId);
        // In a real application, you'd mutate or create a new instance depending on mutability preferences
        restaurantRegistry.put(restaurantId, new Restaurant(restaurant.getId(), restaurant.getName(), restaurant.getLocation(), newMenu));
    }
}