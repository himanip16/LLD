package meetingRoomScheduler.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

// MeetingRoom.java
public class MeetingRoom {
    private final String roomId;
    private final int capacity;
    private final List<Meeting> scheduledMeetings;
    private final ReentrantLock lock; // Fine-grained lock per room to manage concurrent bookings

    public MeetingRoom(String roomId, int capacity) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.scheduledMeetings = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public String getRoomId() {
        return roomId;
    }

    public int getCapacity() {
        return capacity;
    }

    // Returns a copy to maintain thread safety during reading
    public synchronized List<Meeting> getScheduledMeetings() {
        return new ArrayList<>(scheduledMeetings);
    }

    public ReentrantLock getLock() {
        return lock;
    }

    // This checks for overlaps. Must be called while holding the room's lock.
    public boolean isAvailable(long startTime, long endTime) {
        for (Meeting meeting : scheduledMeetings) {
            if (Math.max(meeting.getStartTime(), startTime) < Math.min(meeting.getEndTime(), endTime)) {
                return false; // Overlap detected
            }
        }
        return true;
    }

    // Must be called while holding the room's lock.
    public void addMeeting(Meeting meeting) {
        this.scheduledMeetings.add(meeting);
    }
}
