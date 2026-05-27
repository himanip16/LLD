package meetingRoomScheduler.service;

import meetingRoomScheduler.exception.MeetingSchedulerException;
import meetingRoomScheduler.exception.RoomUnavailableException;
import meetingRoomScheduler.model.BookingRequest;
import meetingRoomScheduler.model.Meeting;
import meetingRoomScheduler.model.MeetingRoom;
import meetingRoomScheduler.observer.NotificationObserver;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// MeetingScheduler.java
public class MeetingScheduler {
    // Concurrent map to allow thread-safe access to room details
    private final Map<String, MeetingRoom> roomRegistry;
    private final List<NotificationObserver> observers;

    public MeetingScheduler(List<MeetingRoom> predefinedRooms) {
        this.roomRegistry = new ConcurrentHashMap<>();
        this.observers = new CopyOnWriteArrayList<>();

        for (MeetingRoom room : predefinedRooms) {
            roomRegistry.put(room.getRoomId(), room);
        }
    }

    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    /**
     * Tries to book a meeting room given a set of acceptable slots/requirements.
     * Thread-Safe: Ensures no double-booking can happen via ReentrantLocks.
     */
    public Meeting bookRoom(List<BookingRequest> requests, List<String> participants, String organizer) {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Booking requests cannot be empty.");
        }

        // Iterate through all alternative options provided by the user
        for (BookingRequest request : requests) {
            for (MeetingRoom room : roomRegistry.values()) {

                // 1. Quick check for capacity filter before locking
                if (room.getCapacity() >= request.getRequiredCapacity()) {

                    // 2. Acquire lock for this specific room to ensure atomic check-and-book execution
                    room.getLock().lock();
                    try {
                        if (room.isAvailable(request.getStartTime(), request.getEndTime())) {

                            // Successfully found an available slot in a qualified room
                            String meetingId = UUID.randomUUID().toString();
                            Meeting meeting = new Meeting(meetingId, request.getStartTime(),
                                    request.getEndTime(), participants, organizer);

                            room.addMeeting(meeting);
                            System.out.println("[Success] Room " + room.getRoomId() + " successfully booked for Meeting ID: " + meetingId);

                            // Trigger Asynchronous notifications to all participants
                            notifyParticipants(meeting);
                            return meeting;
                        }
                    } finally {
                        room.getLock().unlock(); // Ensure lock is released regardless of success/exception
                    }
                }
            }
        }

        // If the system reaches this point, it means no room met the parameters
        throw new RoomUnavailableException("No matching available meeting rooms found for the requested criteria.");
    }

    /**
     * Displays meetings for a given room.
     */
    public void displayCalendarForRoom(String roomId) {
        MeetingRoom room = roomRegistry.get(roomId);
        if (room == null) {
            throw new MeetingSchedulerException("Room ID: " + roomId + " does not exist.");
        }

        List<Meeting> meetings = room.getScheduledMeetings();
        System.out.println("\n--- Calendar for Room: " + roomId + " ---");
        if (meetings.isEmpty()) {
            System.out.println("No meetings scheduled.");
            return;
        }

        for (Meeting m : meetings) {
            System.out.println(String.format("Meeting ID: %s | Start: %d | End: %d | Organizer: %s",
                    m.getId(), m.getStartTime(), m.getEndTime(), m.getOrganizedBy()));
        }
    }

    private void notifyParticipants(Meeting meeting) {
        for (NotificationObserver observer : observers) {
            observer.sendNotification(meeting);
        }
    }
}