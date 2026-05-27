package meetingRoomScheduler.observer;

import meetingRoomScheduler.model.Meeting;

// NotificationObserver.java
public interface NotificationObserver {
    void sendNotification(Meeting meeting);
}
