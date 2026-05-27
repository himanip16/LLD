package meetingRoomScheduler.model;

// BookingRequest.java (Wrapper for requirements array)
public class BookingRequest {
    private final long startTime;
    private final long endTime;
    private final int requiredCapacity;

    public BookingRequest(long startTime, long endTime, int requiredCapacity) {
        if (startTime >= endTime) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.requiredCapacity = requiredCapacity;
    }

    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
    public int getRequiredCapacity() { return requiredCapacity; }
}