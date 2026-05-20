package spotify.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
