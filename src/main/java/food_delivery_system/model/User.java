package food_delivery_system.model;

import lombok.Getter;

@Getter
public abstract class User {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private Location location;

    public User(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}

