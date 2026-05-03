package food_delivery_system.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Restaurant {
    private String id;
    private String name;
    private Location location;
    private Menu menu;
    private boolean isOpen;

    public Restaurant(String id, String name, Location location, Menu menu) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.menu = menu;
        this.isOpen = true;
    }
}