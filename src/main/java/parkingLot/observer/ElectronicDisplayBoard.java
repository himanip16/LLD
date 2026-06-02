package parkingLot.observer;

import parkingLot.vehicle.VehicleType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElectronicDisplayBoard implements ParkingDisplayObserver {
    private final String boardId;
    private final Map<VehicleType, Integer> availabilityMap = new ConcurrentHashMap<>();

    public ElectronicDisplayBoard(String boardId) { this.boardId = boardId; }

    @Override
    public void updateCapacity(VehicleType type, int newCount) {
        availabilityMap.put(type, newCount);
        System.out.println("[DISPLAY BOARD " + boardId + "]: Available Slots -> " + availabilityMap);
    }
}
