package meetingRoomScheduler.observer;

import meetingRoomScheduler.model.Meeting;

public class SlackNotificationObserver implements NotificationObserver {
    @Override
    public void onMeetingBooked(Meeting meeting) {
        System.out.println("Dispatching Slack notification to channel for meeting: " + meeting.getTitle());
    }
}
