package food_delivery_system.model;

import lombok.Getter;

@Getter
public class MenuItem {
    private String itemId;
    private String name;
    private double price;
    private boolean isAvailable;

    public MenuItem(String itemId, String name, double price) {
        this.itemId = itemId;
        this.name = name;
        this.price = price;
    }

}
