package bookMyshow.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Audi {
    private String id;
    private String audiName;
    private List<Seat> physicalSeats; // Every audi has a fixed structural layout
}