package meetingRoomScheduler.observer;

import meetingRoomScheduler.model.Meeting;
import meetingRoomScheduler.model.User;

public class EmailNotificationObserver implements NotificationObserver {
    @Override
    public void onMeetingBooked(Meeting meeting) {
        for (User attendee : meeting.getAttendees()) {
            System.out.println("Sending Email to " + attendee.getEmail() +
                    ": Invitation for '" + meeting.getTitle() + "' in room " + meeting.getRoom().getName());
        }
    }
}
