package meetingRoomScheduler.service;

import meetingRoomScheduler.model.Meeting;
import meetingRoomScheduler.observer.NotificationObserver;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class NotificationService {
    private final List<NotificationObserver> observers = new CopyOnWriteArrayList<>();

    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    public void notifyAll(Meeting meeting) {
        for (NotificationObserver observer : observers) {
            observer.onMeetingBooked(meeting);
        }
    }
}
