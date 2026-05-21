package bookMyshow.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Theatre {
    private String id;
    private String name;
    private Location location;
    private List<Audi> audiList;
}
