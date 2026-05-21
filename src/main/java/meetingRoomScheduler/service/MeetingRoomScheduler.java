package meetingRoomScheduler.service;

import meetingRoomScheduler.model.Meeting;
import meetingRoomScheduler.model.Room;
import meetingRoomScheduler.model.TimeSlot;
import meetingRoomScheduler.model.User;
import meetingRoomScheduler.observer.NotificationObserver;
import meetingRoomScheduler.strategy.RoomAllocationStrategy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MeetingRoomScheduler {
    private final List<Room> rooms;
    private RoomAllocationStrategy allocationStrategy;
    private final NotificationService notificationService;

    public MeetingRoomScheduler(List<Room> rooms, RoomAllocationStrategy strategy) {
        this.rooms = rooms;
        this.allocationStrategy = strategy;
        this.notificationService = new NotificationService();
    }

    public void setStrategy(RoomAllocationStrategy strategy) {
        this.allocationStrategy = strategy;
    }

    public void registerNotificationChannel(NotificationObserver observer) {
        this.notificationService.addObserver(observer);
    }

    public Meeting bookMeeting(String title, TimeSlot slot, int capacity, List<User> attendees) {
        // Step 1: Find a candidate room based on strategy without locking everything
        Optional<Room> candidateRoom = allocationStrategy.allocateLoop(rooms, slot, capacity);

        if (candidateRoom.isEmpty()) {
            throw new IllegalStateException("No suitable room available for this slot and capacity.");
        }

        Room room = candidateRoom.get();

        // Step 2: Thread-Safe Double-Checked Booking Lock
        room.getLock().lock();
        try {
            // Re-verify availability now that we hold the lock
            if (!room.isAvailable(slot)) {
                // If a racing thread snatched it, re-try allocation recursively or bail out
                throw new IllegalStateException("Room was booked by another process. Please retry.");
            }

            Meeting meeting = new Meeting(UUID.randomUUID().toString(), title, slot, room, attendees);
            room.book(meeting);

            // Step 3: Trigger async-capable notifications cleanly away from local state execution
            notificationService.notifyAll(meeting);
            return meeting;

        } finally {
            room.getLock().unlock();
        }
    }

    public List<Meeting> getRoomSchedule(String roomId, LocalDateTime date) {
        return rooms.stream()
                .filter(r -> r.getId().equals(roomId))
                .findFirst()
                .map(r -> r.getDaySchedule(date))
                .orElse(Collections.emptyList());
    }
}
