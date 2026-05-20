package spotify.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Singer {
    private int id;
    private String name;
    public Singer(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
