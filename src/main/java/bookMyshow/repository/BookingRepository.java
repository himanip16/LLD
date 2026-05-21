package bookMyshow.repository;

import bookMyshow.model.Booking;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookingRepository {

    /*
     Simulating Booking table
     */
    private final Map<String, Booking> bookingMap =
            new ConcurrentHashMap<>();

    public Booking save(
            Booking booking
    ) {

        bookingMap.put(
                booking.getId(),
                booking
        );

        return booking;
    }

    public Booking findById(
            String bookingId
    ) {

        Booking booking =
                bookingMap.get(bookingId);

        if (booking == null) {

            throw new IllegalArgumentException(
                    "Booking not found"
            );
        }

        return booking;
    }

    public void delete(
            String bookingId
    ) {

        bookingMap.remove(bookingId);
    }
}