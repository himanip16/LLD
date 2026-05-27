package meetingRoomScheduler;

import meetingRoomScheduler.model.BookingRequest;
import meetingRoomScheduler.model.MeetingRoom;
import meetingRoomScheduler.model.MeetingRoom;
import meetingRoomScheduler.observer.EmailNotificationObserver;
import meetingRoomScheduler.service.MeetingScheduler;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MeetingSchedulerMain {


    public static void main(String[] args) throws InterruptedException {
        // 1. Predefine N rooms with variations in size capacities
        List<MeetingRoom> rooms = Arrays.asList(
                new MeetingRoom("Room-Small", 5),
                new MeetingRoom("Room-Medium", 15),
                new MeetingRoom("Room-Large", 30)
        );

        MeetingScheduler scheduler = new MeetingScheduler(rooms);
        scheduler.registerObserver(new EmailNotificationObserver());

        // 2. Set up Concurrent Thread pool execution environment
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Simulation Task 1: Normal thread booking Room-Small (Slot: 10 to 12)
        executorService.submit(() -> {
            try {
                List<BookingRequest> options = List.of(new BookingRequest(10, 12, 4));
                scheduler.bookRoom(options, List.of("alex@ts.com", "bob@ts.com"), "OrganizerA");
            } catch (Exception e) {
                System.err.println("[Thread 1 Error] " + e.getMessage());
            }
        });

        // Simulation Task 2: Race condition contention thread (Trying to book SAME slot: 10 to 12)
        executorService.submit(() -> {
            try {
                // Sleep slightly to guarantee Task 1 hits lock first
                Thread.sleep(50);
                List<BookingRequest> options = List.of(new BookingRequest(10, 12, 3));
                // Since Room-Small is booked, this will transparently skip to find another eligible room if capacity matches
                scheduler.bookRoom(options, List.of("charlie@ts.com"), "OrganizerB");
            } catch (Exception e) {
                System.err.println("[Thread 2 expected redirect/fail] " + e.getMessage());
            }
        });

        // Simulation Task 3: Booking execution requesting giant capacity
        executorService.submit(() -> {
            try {
                List<BookingRequest> options = List.of(new BookingRequest(14, 16, 25)); // requires 25 people
                scheduler.bookRoom(options, List.of("all-hands@ts.com"), "CEO");
            } catch (Exception e) {
                System.err.println("[Thread 3 Error] " + e.getMessage());
            }
        });

        // Simulation Task 4: Invalid Arguments Handling Check
        executorService.submit(() -> {
            try {
                List<BookingRequest> options = List.of(new BookingRequest(15, 12, 5)); // Start > End
                scheduler.bookRoom(options, List.of("test@ts.com"), "Tester");
            } catch (Exception e) {
                System.out.println("[Thread 4 Expected Exception caught] " + e.getMessage());
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);

        // 3. Print out room state calendars to prove scheduling accuracy
        scheduler.displayCalendarForRoom("Room-Small");
        scheduler.displayCalendarForRoom("Room-Medium");
        scheduler.displayCalendarForRoom("Room-Large");
    }
}