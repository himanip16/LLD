package bookMyshow.model;

import bookMyshow.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Booking {
    private String id;
    private User user;                 // The Customer entity
    private Show show;                 // Rich object context
    private List<ShowSeat> bookedSeats;
    private List<Payment> payments;    // Tracks the 1:N payment attempts
    private BookingStatus bookingStatus;
    private double amount;

    // Clean Constructor accepting Domain Entities, not persistent IDs
    public Booking(User user, Show show, List<ShowSeat> bookedSeats, double amount) {
        this.user = user;
        this.show = show;
        this.bookedSeats = bookedSeats;
        this.amount = amount;
        this.bookingStatus = BookingStatus.PENDING;
        this.payments = new ArrayList<>();
    }

    public void addPaymentAttempt(Payment payment) {
        this.payments.add(payment);
    }
}