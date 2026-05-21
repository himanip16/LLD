package meetingRoomScheduler.observer;

import meetingRoomScheduler.model.Meeting;

public interface NotificationObserver {
    void onMeetingBooked(Meeting meeting);
}
