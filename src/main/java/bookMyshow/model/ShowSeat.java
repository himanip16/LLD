package bookMyshow.model;

import bookMyshow.enums.SeatStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ShowSeat {
    private String id;
    private Seat seat;             // Permanent structural physical seat
    private Show show;             // Which specific runtime execution
    private SeatStatus seatStatus; // VACANT, LOCKED, BOOKED
    private LocalDateTime lockedAt;// Essential for our TTL engine to track expiry
    private Booking booking;
    private double price;


    // Business Method to clear abandoned locks
    public void releaseLock() {
        if (this.seatStatus == SeatStatus.LOCKED) {
            this.seatStatus = SeatStatus.VACANT;
            this.lockedAt = null;
            this.booking = null;
        }
    }

}