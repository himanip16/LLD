package meetingRoomScheduler.strategy;


import meetingRoomScheduler.model.Room;
import meetingRoomScheduler.model.TimeSlot;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BestFitStrategy implements RoomAllocationStrategy {
    @Override
    public Optional<Room> allocateLoop(List<Room> rooms, TimeSlot slot, int requiredCapacity) {
        return rooms.stream()
                .filter(room -> room.getCapacity() >= requiredCapacity)
                .filter(room -> room.isAvailable(slot))
                .min(Comparator.comparingInt(Room::getCapacity));
    }
}
