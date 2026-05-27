package meetingRoomScheduler.observer;

import meetingRoomScheduler.model.Meeting;
import meetingRoomScheduler.model.User;


// EmailNotificationObserver.java
public class EmailNotificationObserver implements NotificationObserver {
    @Override
    public void sendNotification(Meeting meeting) {
        // Simulating sending notification asynchronously
        System.out.println("[Notification] Email sent to participants " + meeting.getParticipants()
                + " for meeting ID: " + meeting.getId());
    }
}
