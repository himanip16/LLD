package meetingRoomScheduler.model;

import java.util.List;

public class Meeting {
    private final String id;
    private final String title;
    private final TimeSlot slot;
    private final Room room;
    private final List<User> attendees;

    public Meeting(String id, String title, TimeSlot slot, Room room, List<User> attendees) {
        this.id = id;
        this.title = title;
        this.slot = slot;
        this.room = room;
        this.attendees = attendees;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public List<User> getAttendees() {
        return attendees;
    }

    public String getTitle() {
        return title;
    }

    public Room getRoom() {
        return room;
    }
}
