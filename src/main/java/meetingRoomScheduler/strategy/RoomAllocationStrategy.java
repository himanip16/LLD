package meetingRoomScheduler.strategy;



import meetingRoomScheduler.model.Room;
import meetingRoomScheduler.model.TimeSlot;

import java.util.List;
import java.util.Optional;

public interface RoomAllocationStrategy {
    Optional<Room> allocateLoop(List<Room> rooms, TimeSlot slot, int requiredCapacity);
}
