package meetingRoomScheduler;

import meetingRoomScheduler.model.Meeting;
import meetingRoomScheduler.model.Room;
import meetingRoomScheduler.model.TimeSlot;
import meetingRoomScheduler.model.User;
import meetingRoomScheduler.observer.EmailNotificationObserver;
import meetingRoomScheduler.observer.SlackNotificationObserver;
import meetingRoomScheduler.service.MeetingRoomScheduler;
import meetingRoomScheduler.strategy.BestFitStrategy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MeetingSchedulerMain {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Initializing Meeting Room Scheduler System ===\n");

        // 1. Setup Sample Data (Predefined Rooms)
        Room boardRoom = new Room("R1", "Board Room", 15);
        Room huddlePod = new Room("R2", "Huddle Pod", 4);
        Room techLab = new Room("R3", "Tech Lab", 10);
        List<Room> corporateRooms = Arrays.asList(boardRoom, huddlePod, techLab);

        // 2. Initialize Scheduler with Best-Fit Strategy
        MeetingRoomScheduler scheduler = new MeetingRoomScheduler(corporateRooms, new BestFitStrategy());

        // 3. Attach Notification Observers (Observer Pattern)
        scheduler.registerNotificationChannel(new EmailNotificationObserver());
        scheduler.registerNotificationChannel(new SlackNotificationObserver());

        // 4. Create Sample Users
        User alice = new User("U1", "Alice Smith", "alice@company.com");
        User bob = new User("U2", "Bob Jones", "bob@company.com");
        User charlie = new User("U3", "Charlie Brown", "charlie@company.com");

        // 5. Test Case 1: Standard Booking Sequence (Validating Best-Fit)
        System.out.println("--- Test Case 1: Booking for 3 People (Should choose Huddle Pod) ---");
        TimeSlot morningSlot = new TimeSlot(
                LocalDateTime.of(2026, 5, 22, 10, 0),
                LocalDateTime.of(2026, 5, 22, 11, 0)
        );

        try {
            Meeting m1 = scheduler.bookMeeting("Standup Sync", morningSlot, 3, Arrays.asList(alice, bob));
            System.out.println("Successfully booked: " + m1.getTitle() + " in " + m1.getRoom().getName());
        } catch (Exception e) {
            System.err.println("Booking failed: " + e.getMessage());
        }
        System.out.println();

        // 6. Test Case 2: Concurrent Race Condition Simulation
        System.out.println("--- Test Case 2: Concurrent Race Condition (2 Users, 1 Room, Same Slot) ---");

        // This slot directly overlaps the previous huddle room booking, forcing both users
        // to look for a remaining room matching a required capacity of 8 (Tech Lab).
        TimeSlot clashSlot = new TimeSlot(
                LocalDateTime.of(2026, 5, 22, 10, 30),
                LocalDateTime.of(2026, 5, 22, 11, 30)
        );

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // User A trying to book a Architecture Review
        Runnable bookingTaskA = () -> {
            try {
                Meeting mA = scheduler.bookMeeting("Architecture Review", clashSlot, 8, Arrays.asList(alice, charlie));
                System.out.println("[SUCCESS] Thread A booked: " + mA.getTitle() + " in " + mA.getRoom().getName());
            } catch (Exception e) {
                System.out.println("[BLOCKED] Thread A failed to book: " + e.getMessage());
            }
        };

        // User B trying to book a Sprint Planning at the exact same moment
        Runnable bookingTaskB = () -> {
            try {
                Meeting mB = scheduler.bookMeeting("Sprint Planning", clashSlot, 8, Arrays.asList(bob, charlie));
                System.out.println("[SUCCESS] Thread B booked: " + mB.getTitle() + " in " + mB.getRoom().getName());
            } catch (Exception e) {
                System.out.println("[BLOCKED] Thread B failed to book: " + e.getMessage());
            }
        };

        // Fire threads simultaneously
        executor.submit(bookingTaskA);
        executor.submit(bookingTaskB);

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println();

        // 7. Test Case 3: Display Today's Room Calendar Schedules
        System.out.println("--- Test Case 3: Printing Room Schedules for May 22, 2026 ---");
        LocalDateTime targetDay = LocalDateTime.of(2026, 5, 22, 0, 0);

        for (Room room : corporateRooms) {
            System.out.println("Schedule for Room [" + room.getName() + "]:");
            List<Meeting> schedule = scheduler.getRoomSchedule(room.getId(), targetDay);
            if (schedule.isEmpty()) {
                System.out.println("  (No meetings scheduled)");
            } else {
                for (Meeting m : schedule) {
                    System.out.println("  - " + m.getSlot().getStartTime().toLocalTime() + " to "
                            + m.getSlot().getEndTime().toLocalTime() + " : " + m.getTitle());
                }
            }
        }
    }
}

