package meetingRoomScheduler.model;

import lombok.Getter;
import meetingRoomScheduler.model.Meeting;
import meetingRoomScheduler.service.MeetingRoomCalendar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Room {
    private final String id;
    private final String name;
    private final int capacity;
    private final MeetingRoomCalendar calendar;
    // Fine-grained lock per room to maximize concurrency
    private final ReentrantLock lock = new ReentrantLock();

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.calendar = new MeetingRoomCalendar();
    }


    public boolean isAvailable(TimeSlot slot) {
        return calendar.isAvailable(slot);
    }

    public void book(Meeting meeting) {
        calendar.addMeeting(meeting);
    }

    public List<Meeting> getDaySchedule(LocalDateTime date) {
        return calendar.getScheduleForDay(date);
    }
}
