package meetingRoomScheduler.model;

import java.util.ArrayList;
import java.util.List;

// Meeting.java
public class Meeting {
    private final String id;
    private final long startTime;
    private final long endTime;
    private final List<String> participants;
    private final String organizedBy;

    public Meeting(String id, long startTime, long endTime, List<String> participants, String organizedBy) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = new ArrayList<>(participants);
        this.organizedBy = organizedBy;
    }

    public String getId() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public String getOrganizedBy() {
        return organizedBy;
    }
}
