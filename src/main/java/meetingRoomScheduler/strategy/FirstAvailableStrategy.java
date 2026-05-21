package meetingRoomScheduler.strategy;



import meetingRoomScheduler.model.Room;
import meetingRoomScheduler.model.TimeSlot;

import java.util.List;
import java.util.Optional;

class FirstAvailableStrategy implements RoomAllocationStrategy {
    @Override
    public Optional<Room> allocateLoop(List<Room> rooms, TimeSlot slot, int requiredCapacity) {
        return rooms.stream()
                .filter(room -> room.getCapacity() >= requiredCapacity)
                .filter(room -> room.isAvailable(slot))
                .findFirst();
    }
}
