package bookMyshow.model;


import bookMyshow.enums.SeatType; // e.g., GOLD, PLATINUM, SILVER
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Seat {
    private String id;
    private String seatNumber; // e.g., "A12"
    private SeatType seatType;
}

