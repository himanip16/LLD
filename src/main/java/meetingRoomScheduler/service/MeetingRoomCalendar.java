package meetingRoomScheduler.service;

import meetingRoomScheduler.model.Meeting;
import meetingRoomScheduler.model.TimeSlot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MeetingRoomCalendar {
    private final List<Meeting> meetings = new ArrayList<>();

    public synchronized boolean isAvailable(TimeSlot slot) {
        for (Meeting meeting : meetings) {
            if (meeting.getSlot().overlaps(slot)) {
                return false;
            }
        }
        return true;
    }

    public synchronized void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    public synchronized List<Meeting> getScheduleForDay(LocalDateTime date) {
        return meetings.stream()
                .filter(m -> m.getSlot().getStartTime().toLocalDate().equals(date.toLocalDate()))
                .sorted(Comparator.comparing(m -> m.getSlot().getStartTime()))
                .collect(Collectors.toList());
    }
}
