package parkingLot.model;

import parkingLot.spot.ParkingSpot;
import parkingLot.vehicle.VehicleType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class ParkingFloor {
    private final int floorNumber;
    private final Map<VehicleType, PriorityBlockingQueue<ParkingSpot>> freeSpotsMap = new ConcurrentHashMap<>();

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        for (VehicleType type : VehicleType.values()) {
            freeSpotsMap.put(type, new PriorityBlockingQueue<>());
        }
    }
    public void addSpot(ParkingSpot spot) { freeSpotsMap.get(spot.getSpotType()).add(spot); }
    public PriorityBlockingQueue<ParkingSpot> getFreeSpots(VehicleType type) { return freeSpotsMap.get(type); }
    public int getFloorNumber() { return floorNumber; }
}
