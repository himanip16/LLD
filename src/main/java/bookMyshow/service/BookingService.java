package bookMyshow.service;

import bookMyshow.enums.BookingStatus;
import bookMyshow.enums.PaymentStatus;
import bookMyshow.enums.SeatStatus;
import bookMyshow.model.Booking;
import bookMyshow.model.Payment;
import bookMyshow.model.Show;
import bookMyshow.model.ShowSeat;
import bookMyshow.model.User;
import bookMyshow.repository.BookingRepository;
import bookMyshow.repository.ShowSeatRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BookingService {

    private final ShowSeatRepository showSeatRepository;
    private final BookingRepository bookingRepository;

    /*
     One lock object per show.
     Users booking different shows should not block each other.
     */
    private final ConcurrentHashMap<String, Object> showLocks = new ConcurrentHashMap<>();

    /*
     Example:
     Seat lock valid for 10 mins
     */
    private static final long LOCK_TIMEOUT_MINUTES = 10;

    public BookingService(ShowSeatRepository showSeatRepository, BookingRepository bookingRepository) {
        this.showSeatRepository = showSeatRepository;
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(
            User user,
            Show show,
            List<String> showSeatIds
    ) {

        /*
         Get dedicated lock object for this show execution
         */
        Object showLock =
                showLocks.computeIfAbsent(
                        show.getId(),
                        id -> new Object()
                );

        /*
         Only one thread can mutate same show's seats
         simultaneously
         */
        synchronized (showLock) {

            /*
             Fetch runtime seats
             */
            List<ShowSeat> showSeats =
                    showSeatRepository.findByIds(showSeatIds);

            /*
             Step 1:
             Release expired locks before validation
             */
            for (ShowSeat seat : showSeats) {

                releaseExpiredLockIfRequired(seat);
            }

            /*
             Step 2:
             Validate all seats are available
             */
            for (ShowSeat seat : showSeats) {

                if (seat.getSeatStatus() != SeatStatus.VACANT) {

                    throw new IllegalStateException(
                            "Seat already occupied: "
                                    + seat.getSeat().getSeatNumber()
                    );
                }
            }

            /*
             Step 3:
             Create booking object
             */
            Booking booking = new Booking(
                    user,
                    show,
                    showSeats,
                    calculateAmount(showSeats)
            );

            booking.setId(UUID.randomUUID().toString());

            /*
             Step 4:
             Lock seats
             */
            for (ShowSeat seat : showSeats) {

                seat.setBooking(booking);

                seat.setSeatStatus(SeatStatus.LOCKED);

                seat.setLockedAt(LocalDateTime.now());
            }

            /*
             Persist updated seat states
             */
            showSeatRepository.saveAll(showSeats);

            /*
             Persist booking
             */
            return bookingRepository.save(booking);
        }
    }

    /*
     Payment succeeded
     LOCKED -> BOOKED
     */
    public void confirmBooking(
            Booking booking,
            Payment payment
    ) {

        for (ShowSeat seat : booking.getBookedSeats()) {

            if (seat.getSeatStatus() != SeatStatus.LOCKED) {

                throw new IllegalStateException(
                        "Seat not in locked state"
                );
            }

            seat.setSeatStatus(SeatStatus.BOOKED);
        }

        payment.setPaymentStatus(PaymentStatus.SUCCESS);

        booking.addPaymentAttempt(payment);

        booking.setBookingStatus(BookingStatus.CONFIRMED);

        showSeatRepository.saveAll(booking.getBookedSeats());

        bookingRepository.save(booking);
    }

    /*
     Payment failed
     LOCKED -> VACANT
     */
    public void failBooking(
            Booking booking,
            Payment payment
    ) {

        for (ShowSeat seat : booking.getBookedSeats()) {

            seat.releaseLock();
        }

        payment.setPaymentStatus(PaymentStatus.FAILED);

        booking.addPaymentAttempt(payment);

        booking.setBookingStatus(BookingStatus.FAILED);

        showSeatRepository.saveAll(booking.getBookedSeats());

        bookingRepository.save(booking);
    }

    /*
     Cleanup logic for abandoned seats
     */
    private void releaseExpiredLockIfRequired(
            ShowSeat seat
    ) {

        if (seat.getSeatStatus() != SeatStatus.LOCKED) {
            return;
        }

        LocalDateTime lockedAt = seat.getLockedAt();

        if (lockedAt == null) {
            return;
        }

        long minutes =
                Duration.between(
                        lockedAt,
                        LocalDateTime.now()
                ).toMinutes();

        if (minutes >= LOCK_TIMEOUT_MINUTES) {

            seat.releaseLock();
        }
    }

    private double calculateAmount(
            List<ShowSeat> showSeats
    ) {

        return showSeats.stream()
                .mapToDouble(ShowSeat::getPrice)
                .sum();
    }
}